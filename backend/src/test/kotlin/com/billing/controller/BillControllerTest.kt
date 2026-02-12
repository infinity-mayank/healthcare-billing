package com.billing.controller

import com.billing.TestDatabaseCleaner
import com.billing.dto.BillResponse
import com.billing.dto.DoctorRegistrationRequest
import com.billing.dto.PatientRegistrationRequest
import com.billing.dto.PatientResponse
import com.billing.dto.DoctorResponse
import com.billing.dto.SaveBillRequest
import com.billing.dto.SaveBillResponse
import io.micronaut.http.*
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

@MicronautTest
class BillControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var dbCleaner: TestDatabaseCleaner

    @BeforeEach
    fun setup() {
        dbCleaner.clean("patients")
        dbCleaner.clean("doctors")
        dbCleaner.clean("bills")
    }

    @Test
    fun `should generate bill and return 200`() {
        val patientRequest = PatientRegistrationRequest(
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = "01/01/1990",
            insuranceBIN = "123456",
            insurancePCN = "999888",
            insuranceMemberID = "2229838"
        )
        val patientCreateRequest = HttpRequest.POST("/api/patients", patientRequest)
            .contentType(MediaType.APPLICATION_JSON)
        val patientResponse = client.toBlocking()
            .exchange(patientCreateRequest, PatientResponse::class.java)
        val patientId = patientResponse.body()!!.id

        val doctorRequest = DoctorRegistrationRequest(
            firstName = "Jane",
            lastName = "Smith",
            npiNumber = "1234567890",
            specialty = "CARDIO",
            practiceStartDate = "01/15/2020"
        )
        val doctorCreateRequest = HttpRequest.POST("/api/doctors", doctorRequest)
            .contentType(MediaType.APPLICATION_JSON)
        val doctorResponse = client.toBlocking()
            .exchange(doctorCreateRequest, DoctorResponse::class.java)
        val doctorNpiNumber = doctorResponse.body().npiNumber

        val billRequest = HttpRequest.GET<Any>("/api/bill/generate?patientId=$patientId&doctorNpiNumber=$doctorNpiNumber")
        val billResponse = client.toBlocking()
            .exchange(billRequest, BillResponse::class.java)

        assertEquals(HttpStatus.OK, billResponse.status)

        val billing = billResponse.body()
        assertEquals(patientId, billing.patientId)
        assertEquals(doctorNpiNumber, billing.doctorNpiNumber)
        assertEquals(1000.0, billing.consultationFee)
        assertEquals(120.0, billing.taxAmount)
        assertEquals(1120.0, billing.totalAmount)
        assertEquals(0.0, billing.discountAmount)
        assertEquals(0.0, billing.discountPercentage)
    }

    @Test
    fun `should generate bill with discounts and return 200`() {
        val patientRequest = PatientRegistrationRequest(
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = "01/01/1990",
            insuranceBIN = "123456",
            insurancePCN = "999888",
            insuranceMemberID = "2229838"
        )
        val patientCreateRequest = HttpRequest.POST("/api/patients", patientRequest)
            .contentType(MediaType.APPLICATION_JSON)
        val patientResponse = client.toBlocking()
            .exchange(patientCreateRequest, PatientResponse::class.java)
        val patientId = patientResponse.body()!!.id

        val doctorRequest = DoctorRegistrationRequest(
            firstName = "Jane",
            lastName = "Smith",
            npiNumber = "1234567890",
            specialty = "CARDIO",
            practiceStartDate = "01/15/2020"
        )
        val doctorCreateRequest = HttpRequest.POST("/api/doctors", doctorRequest)
            .contentType(MediaType.APPLICATION_JSON)
        val doctorResponse = client.toBlocking()
            .exchange(doctorCreateRequest, DoctorResponse::class.java)
        val doctorNpiNumber = doctorResponse.body().npiNumber

        val firstBillRequest = HttpRequest.GET<Any>("/api/bill/generate?patientId=$patientId&doctorNpiNumber=$doctorNpiNumber")
        val firstBillResponse = client.toBlocking()
            .exchange(firstBillRequest, BillResponse::class.java)
        val firstBill = firstBillResponse.body()

        val saveBillRequest = SaveBillRequest(
            patientId = firstBill.patientId,
            doctorNpiNumber = firstBill.doctorNpiNumber,
            consultationFee = firstBill.consultationFee,
            discountAmount = firstBill.discountAmount,
            discountPercentage = firstBill.discountPercentage,
            taxAmount = firstBill.taxAmount,
            totalAmount = firstBill.totalAmount,
            coPayAmount = firstBill.coPayAmount,
            insurancePayableAmount = firstBill.insurancePayableAmount,
            taxRatePercentage = firstBill.taxRatePercentage,
            coPayRatePercentage = firstBill.coPayRatePercentage
        )
        client.toBlocking()
            .exchange(HttpRequest.POST("/api/bill/save", saveBillRequest), SaveBillResponse::class.java)

        val secondBillRequest = HttpRequest.GET<Any>("/api/bill/generate?patientId=$patientId&doctorNpiNumber=$doctorNpiNumber")
        val billResponse = client.toBlocking()
            .exchange(secondBillRequest, BillResponse::class.java)

        assertEquals(HttpStatus.OK, billResponse.status)

        val billing = billResponse.body()
        assertEquals(patientId, billing.patientId)
        assertEquals(doctorNpiNumber, billing.doctorNpiNumber)
        assertEquals(1000.0, billing.consultationFee)
        assertEquals(118.8, billing.taxAmount)
        assertEquals(1108.8, billing.totalAmount)
        assertEquals(1.0, billing.discountPercentage)
        assertEquals(10.0, billing.discountAmount)
    }

    @Test
    fun `should return 404 when patient not found`() {
        val doctorRequest = DoctorRegistrationRequest(
            firstName = "Jane",
            lastName = "Smith",
            npiNumber = "1234567890",
            specialty = "CARDIO",
            practiceStartDate = "01/15/2020"
        )
        val doctorCreateRequest = HttpRequest.POST("/api/doctors", doctorRequest)
            .contentType(MediaType.APPLICATION_JSON)
        val doctorResponse = client.toBlocking()
            .exchange(doctorCreateRequest, DoctorResponse::class.java)
        val doctorNpiNumber = doctorResponse.body()!!.npiNumber

        val invalidPatientId = "00000000-0000-0000-0000-000000000000"
        val billRequest = HttpRequest.GET<Any>("/api/bill/generate?patientId=$invalidPatientId&doctorNpiNumber=$doctorNpiNumber")

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(billRequest, BillResponse::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
    }

    @Test
    fun `should return 404 when doctor not found`() {
        val patientRequest = PatientRegistrationRequest(
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = "01/01/1990",
            insuranceBIN = "123456",
            insurancePCN = "999888",
            insuranceMemberID = "2229838"
        )
        val patientCreateRequest = HttpRequest.POST("/api/patients", patientRequest)
            .contentType(MediaType.APPLICATION_JSON)
        val patientResponse = client.toBlocking()
            .exchange(patientCreateRequest, PatientResponse::class.java)
        val patientId = patientResponse.body()!!.id

        val invalidDoctorNpi = "9999999999"
        val billRequest = HttpRequest.GET<Any>("/api/bill/generate?patientId=$patientId&doctorNpiNumber=$invalidDoctorNpi")

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(billRequest, BillResponse::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
    }

    @Test
    fun `should return 400 when patientId format is invalid`() {
        val invalidPatientId = "invalid-uuid"
        val doctorNpiNumber = "1234567890"
        val billRequest = HttpRequest.GET<Any>("/api/bill/generate?patientId=$invalidPatientId&doctorNpiNumber=$doctorNpiNumber")

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(billRequest, BillResponse::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `should return 400 when patientId is missing`() {
        val doctorNpiNumber = "1234567890"
        val billRequest = HttpRequest.GET<Any>("/api/bill/generate?doctorNpiNumber=$doctorNpiNumber")

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(billRequest, BillResponse::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `should return 400 when doctorNpiNumber is missing`() {
        val patientId = UUID.randomUUID();
        val billRequest = HttpRequest.GET<Any>("/api/bill/generate?patientId=$patientId")

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(billRequest, BillResponse::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `should save bill and return 201`() {
        val patientRequest = PatientRegistrationRequest(
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = "01/01/1990",
            insuranceBIN = "123456",
            insurancePCN = "999888",
            insuranceMemberID = "2229838"
        )
        val patientResponse = client.toBlocking()
            .exchange(HttpRequest.POST("/api/patients", patientRequest), PatientResponse::class.java)
        val patientId = patientResponse.body()!!.id

        val doctorRequest = DoctorRegistrationRequest(
            firstName = "Jane",
            lastName = "Smith",
            npiNumber = "1234567890",
            specialty = "CARDIO",
            practiceStartDate = "01/15/2020"
        )
        client.toBlocking().exchange(HttpRequest.POST("/api/doctors", doctorRequest), DoctorResponse::class.java)

        val saveBillRequest = SaveBillRequest(
            patientId = patientId,
            doctorNpiNumber = "1234567890",
            consultationFee = 1000.0,
            taxAmount = 108.0,
            totalAmount = 1108.0,
            coPayAmount = 110.8,
            insurancePayableAmount = 997.2,
            taxRatePercentage = 12.0,
            coPayRatePercentage = 10.0,
            discountAmount = 100.0,
            discountPercentage = 10.0,
        )

        val response = client.toBlocking()
            .exchange(HttpRequest.POST("/api/bill/save", saveBillRequest), SaveBillResponse::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertNotNull(response.body()!!.id)
        assertNotNull(response.body()!!.createdAt)
    }

    @Test
    fun `should return 400 when saving bill with invalid patient ID format`() {
        val saveBillRequest = SaveBillRequest(
            patientId = "invalid-uuid",
            doctorNpiNumber = "1234567890",
            consultationFee = 1000.0,
            taxAmount = 120.0,
            totalAmount = 1120.0,
            coPayAmount = 112.0,
            insurancePayableAmount = 1008.0,
            taxRatePercentage = 12.0,
            coPayRatePercentage = 10.0,
            discountAmount = 0.0,
            discountPercentage = 0.0,
        )

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(HttpRequest.POST("/api/bill/save", saveBillRequest), SaveBillResponse::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `should return 404 when saving bill when patient is not available`() {
        val doctorRequest = DoctorRegistrationRequest(
            firstName = "Jane",
            lastName = "Smith",
            npiNumber = "1234567890",
            specialty = "CARDIO",
            practiceStartDate = "01/15/2020"
        )
        client.toBlocking().exchange(HttpRequest.POST("/api/doctors", doctorRequest), DoctorResponse::class.java)

        val saveBillRequest = SaveBillRequest(
            patientId = UUID.randomUUID().toString(),
            doctorNpiNumber = "1234567890",
            consultationFee = 1000.0,
            taxAmount = 120.0,
            totalAmount = 1120.0,
            coPayAmount = 112.0,
            insurancePayableAmount = 1008.0,
            taxRatePercentage = 12.0,
            coPayRatePercentage = 10.0,
            discountAmount = 0.0,
            discountPercentage = 0.0,
        )

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(HttpRequest.POST("/api/billing/save", saveBillRequest), SaveBillResponse::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
    }

    @Test
    fun `should return 404 when saving bill when doctor is not available`() {
        val patientRequest = PatientRegistrationRequest(
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = "01/01/1990",
            insuranceBIN = "123456",
            insurancePCN = "999888",
            insuranceMemberID = "2229838"
        )
        val patientResponse = client.toBlocking()
            .exchange(HttpRequest.POST("/api/patients", patientRequest), PatientResponse::class.java)
        val patientId = patientResponse.body()!!.id

        val saveBillRequest = SaveBillRequest(
            patientId = patientId,
            doctorNpiNumber = "9999999999",
            consultationFee = 1000.0,
            taxAmount = 120.0,
            totalAmount = 1120.0,
            coPayAmount = 112.0,
            insurancePayableAmount = 1008.0,
            taxRatePercentage = 12.0,
            coPayRatePercentage = 10.0,
            discountAmount = 0.0,
            discountPercentage = 0.0,
        )

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(HttpRequest.POST("/api/bill/save", saveBillRequest), SaveBillResponse::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
    }
}

