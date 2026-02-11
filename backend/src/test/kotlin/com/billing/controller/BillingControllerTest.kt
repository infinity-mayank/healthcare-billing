package com.billing.controller

import com.billing.TestDatabaseCleaner
import com.billing.dto.BillingResponse
import com.billing.dto.DoctorRegistrationRequest
import com.billing.dto.PatientRegistrationRequest
import com.billing.dto.PatientResponse
import com.billing.dto.DoctorResponse
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
class BillingControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var dbCleaner: TestDatabaseCleaner

    @BeforeEach
    fun setup() {
        dbCleaner.clean("patients")
        dbCleaner.clean("doctors")
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

        val billRequest = HttpRequest.GET<Any>("/api/billing/generate?patientId=$patientId&doctorNpiNumber=$doctorNpiNumber")
        val billResponse = client.toBlocking()
            .exchange(billRequest, BillingResponse::class.java)

        assertEquals(HttpStatus.OK, billResponse.status)

        val billing = billResponse.body()
        assertEquals(patientId, billing.patientId)
        assertEquals(doctorNpiNumber, billing.doctorNpiNumber)
        assertEquals(1000.0, billing.consultationFee)
        assertEquals(120.0, billing.taxAmount)
        assertEquals(1120.0, billing.totalAmount)

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
        val billRequest = HttpRequest.GET<Any>("/api/billing/generate?patientId=$invalidPatientId&doctorNpiNumber=$doctorNpiNumber")

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(billRequest, BillingResponse::class.java)
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
        val billRequest = HttpRequest.GET<Any>("/api/billing/generate?patientId=$patientId&doctorNpiNumber=$invalidDoctorNpi")

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(billRequest, BillingResponse::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
    }

    @Test
    fun `should return 400 when patientId format is invalid`() {
        val invalidPatientId = "invalid-uuid"
        val doctorNpiNumber = "1234567890"
        val billRequest = HttpRequest.GET<Any>("/api/billing/generate?patientId=$invalidPatientId&doctorNpiNumber=$doctorNpiNumber")

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(billRequest, BillingResponse::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `should return 400 when patientId is missing`() {
        val doctorNpiNumber = "1234567890"
        val billRequest = HttpRequest.GET<Any>("/api/billing/generate?doctorNpiNumber=$doctorNpiNumber")

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(billRequest, BillingResponse::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `should return 400 when doctorNpiNumber is missing`() {
        val patientId = UUID.randomUUID();
        val billRequest = HttpRequest.GET<Any>("/api/billing/generate?patientId=$patientId")

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(billRequest, BillingResponse::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }
}

