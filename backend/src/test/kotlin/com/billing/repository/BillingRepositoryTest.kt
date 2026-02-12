package com.billing.repository

import com.billing.entity.BillEntity
import com.billing.entity.DoctorEntity
import com.billing.entity.PatientEntity
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

@MicronautTest
class BillingRepositoryTest {

    @Inject
    lateinit var repository: BillingRepository

    @Inject
    lateinit var patientRepository: PatientRepository

    @Inject
    lateinit var doctorRepository: DoctorRepository

    @AfterEach
    fun cleanup() {
        repository.deleteAll()
        patientRepository.deleteAll()
        doctorRepository.deleteAll()
    }

    @Test
    fun `should save bill successfully`() {
        val patient = PatientEntity(
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = LocalDate.of(1990, 1, 1),
            insuranceBIN = "123456",
            insurancePCN = "320984",
            insuranceMemberID = "823873"
        )
        val savedPatient = patientRepository.save(patient)

        val doctor = DoctorEntity(
            npiNumber = "1234567890",
            firstName = "Jane",
            lastName = "Smith",
            specialty = "CARDIO",
            practiceStartDate = LocalDate.of(2015, 1, 15)
        )
        doctorRepository.save(doctor)

        val bill = BillEntity(
            patientId = savedPatient.id,
            doctorNpiNumber = "1234567890",
            consultationFee = 1000.0,
            taxAmount = 120.0,
            totalAmount = 1120.0,
            coPayAmount = 112.0,
            insurancePayableAmount = 1008.0,
            taxRatePercentage = 12.0,
            coPayRatePercentage = 10.0
        )

        val saved = repository.save(bill)

        assertNotNull(saved.id)
        assertEquals(bill.patientId, saved.patientId)
        assertEquals(bill.doctorNpiNumber, saved.doctorNpiNumber)
        assertEquals(bill.consultationFee, saved.consultationFee)
        assertEquals(bill.taxAmount, saved.taxAmount)
        assertEquals(bill.totalAmount, saved.totalAmount)
        assertEquals(bill.coPayAmount, saved.coPayAmount)
        assertEquals(bill.insurancePayableAmount, saved.insurancePayableAmount)
        assertEquals(bill.taxRatePercentage, saved.taxRatePercentage)
        assertEquals(bill.coPayRatePercentage, saved.coPayRatePercentage)
        assertNotNull(saved.createdAt)
    }
}


