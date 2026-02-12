package com.billing.service

import com.billing.config.BillingConfiguration
import com.billing.dto.SaveBillRequest
import com.billing.entity.BillEntity
import com.billing.entity.DoctorEntity
import com.billing.entity.PatientEntity
import com.billing.repository.BillRepository
import com.billing.repository.DoctorRepository
import com.billing.repository.PatientRepository
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class BillServiceTest {

    private val patientRepository = mock<PatientRepository>()
    private val doctorRepository = mock<DoctorRepository>()
    private val billRepository = mock<BillRepository>()
    private val billingConfiguration = BillingConfiguration().apply { taxRate = 0.12; coPayRate = 0.10 }
    private val billService = BillService(doctorRepository, patientRepository, billingConfiguration, billRepository)

    @Test
    fun `should generate bill successfully`() {
        val patientId = UUID.randomUUID()
        val patientEntity = PatientEntity(
            id = patientId,
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = LocalDate.of(1990, 1, 1),
            insuranceBIN = "123456",
            insurancePCN = "999888",
            insuranceMemberID = "2229838"
        )

        val doctorEntity = DoctorEntity(
            npiNumber = "1234567890",
            firstName = "Jane",
            lastName = "Smith",
            specialty = "CARDIO",
            practiceStartDate = LocalDate.of(2015, 1, 15)
        )

        whenever(patientRepository.findById(patientId)).thenReturn(Optional.of(patientEntity))
        whenever(doctorRepository.findById("1234567890")).thenReturn(Optional.of(doctorEntity))

        val response = billService.generateBill(patientId.toString(), "1234567890")

        assertEquals(patientId.toString(), response.patientId)
        assertEquals("1234567890", response.doctorNpiNumber)
        assertEquals(1000.0, response.consultationFee)
        assertEquals(120.0, response.taxAmount)
        assertEquals(1120.0, response.totalAmount)
        assertEquals(112.0, response.coPayAmount)
        assertEquals(1008.0, response.insurancePayableAmount)
        assertEquals(12.0, response.taxRatePercentage)
        assertEquals(10.0, response.coPayRatePercentage)
    }

    @Test
    fun `should throw exception when patient not found`() {
        val patientId = UUID.randomUUID()
        whenever(patientRepository.findById(patientId)).thenReturn(Optional.empty())

        val exception = assertThrows<HttpStatusException> {
            billService.generateBill(patientId.toString(), "1234567890")
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
        assertEquals("Patient not found with ID: $patientId", exception.message)
    }

    @Test
    fun `should throw exception when doctor not found`() {
        val patientId = UUID.randomUUID()
        val patientEntity = PatientEntity(
            id = patientId,
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = LocalDate.of(1990, 1, 1),
            insuranceBIN = "123456",
            insurancePCN = "999888",
            insuranceMemberID = "2229838"
        )

        whenever(patientRepository.findById(patientId)).thenReturn(Optional.of(patientEntity))
        whenever(doctorRepository.findById("1234567890")).thenReturn(Optional.empty())

        val exception = assertThrows<HttpStatusException> {
            billService.generateBill(patientId.toString(), "1234567890")
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
        assertEquals("Doctor not found with NPI number: 1234567890", exception.message)
    }

    @Test
    fun `should throw exception when patient ID format is invalid`() {
        val exception = assertThrows<HttpStatusException> {
            billService.generateBill("invalid-uuid", "1234567890")
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
        assertEquals("Invalid patient ID format", exception.message)
    }

    @Test
    fun `should save bill successfully`() {
        val patientId = UUID.randomUUID()
        val patientEntity = PatientEntity(
            id = patientId,
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = LocalDate.of(1990, 1, 1),
            insuranceBIN = "123456",
            insurancePCN = "999888",
            insuranceMemberID = "2229838"
        )

        val doctorEntity = DoctorEntity(
            npiNumber = "1234567890",
            firstName = "Jane",
            lastName = "Smith",
            specialty = "CARDIO",
            practiceStartDate = LocalDate.of(2015, 1, 15)
        )

        val savedBillEntity = BillEntity(
            id = UUID.randomUUID(),
            patientId = patientId,
            doctorNpiNumber = "1234567890",
            consultationFee = 1000.0,
            taxAmount = 120.0,
            totalAmount = 1120.0,
            coPayAmount = 112.0,
            insurancePayableAmount = 1008.0,
            taxRatePercentage = 12.0,
            coPayRatePercentage = 10.0,
            createdAt = LocalDateTime.now()
        )

        whenever(patientRepository.findById(patientId)).thenReturn(Optional.of(patientEntity))
        whenever(doctorRepository.findById("1234567890")).thenReturn(Optional.of(doctorEntity))
        whenever(billRepository.save(any())).thenReturn(savedBillEntity)

        val request = SaveBillRequest(
            patientId = patientId.toString(),
            doctorNpiNumber = "1234567890",
            consultationFee = 1000.0,
            taxAmount = 120.0,
            totalAmount = 1120.0,
            coPayAmount = 112.0,
            insurancePayableAmount = 1008.0,
            taxRatePercentage = 12.0,
            coPayRatePercentage = 10.0
        )

        val response = billService.saveBill(request)

        assertNotNull(response.id)
        assertNotNull(response.createdAt)
    }

    @Test
    fun `should throw exception when saving bill with invalid patient ID format`() {
        val request = SaveBillRequest(
            patientId = "invalid-uuid",
            doctorNpiNumber = "1234567890",
            consultationFee = 1000.0,
            taxAmount = 120.0,
            totalAmount = 1120.0,
            coPayAmount = 112.0,
            insurancePayableAmount = 1008.0,
            taxRatePercentage = 12.0,
            coPayRatePercentage = 10.0
        )

        val exception = assertThrows<HttpStatusException> {
            billService.saveBill(request)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
        assertEquals("Invalid patient ID format", exception.message)
    }

    @Test
    fun `should throw exception when saving bill when patient is not available`() {
        val patientId = UUID.randomUUID()
        whenever(patientRepository.findById(patientId)).thenReturn(Optional.empty())

        val request = SaveBillRequest(
            patientId = patientId.toString(),
            doctorNpiNumber = "1234567890",
            consultationFee = 1000.0,
            taxAmount = 120.0,
            totalAmount = 1120.0,
            coPayAmount = 112.0,
            insurancePayableAmount = 1008.0,
            taxRatePercentage = 12.0,
            coPayRatePercentage = 10.0
        )

        val exception = assertThrows<HttpStatusException> {
            billService.saveBill(request)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
        assertEquals("Patient not found with ID: ${patientId}", exception.message)
    }

    @Test
    fun `should throw exception when saving bill when doctor is not available`() {
        val patientId = UUID.randomUUID()
        val patientEntity = PatientEntity(
            id = patientId,
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = LocalDate.of(1990, 1, 1),
            insuranceBIN = "123456",
            insurancePCN = "999888",
            insuranceMemberID = "2229838"
        )

        whenever(patientRepository.findById(patientId)).thenReturn(Optional.of(patientEntity))
        whenever(doctorRepository.findById("1234567890")).thenReturn(Optional.empty())

        val request = SaveBillRequest(
            patientId = patientId.toString(),
            doctorNpiNumber = "1234567890",
            consultationFee = 1000.0,
            taxAmount = 120.0,
            totalAmount = 1120.0,
            coPayAmount = 112.0,
            insurancePayableAmount = 1008.0,
            taxRatePercentage = 12.0,
            coPayRatePercentage = 10.0
        )

        val exception = assertThrows<HttpStatusException> {
            billService.saveBill(request)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
        assertEquals("Doctor not found with NPI number: 1234567890", exception.message)
    }
}

