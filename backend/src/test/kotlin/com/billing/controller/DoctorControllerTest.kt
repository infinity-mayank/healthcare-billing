package com.billing.controller

import com.billing.dto.DoctorRegistrationRequest
import com.billing.dto.DoctorResponse
import io.micronaut.http.*
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@MicronautTest
class DoctorControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    private fun validRequest() =
        DoctorRegistrationRequest(
            firstName = "Jane",
            lastName = "Smith",
            npiNumber = "1234567890",
            specialty = "CARDIO",
            practiceStartDate = "01/15/2020"
        )

    @Test
    fun `should register doctor and return 201`() {
        val request = HttpRequest.POST("/api/doctors", validRequest())
            .contentType(MediaType.APPLICATION_JSON)

        val response = client.toBlocking()
            .exchange(request, DoctorResponse::class.java)

        assertEquals(HttpStatus.CREATED, response.status)

        val responseBody = response.body()
        assertEquals(validRequest().npiNumber, responseBody?.npiNumber)
        assertEquals(validRequest().firstName, responseBody?.firstName)
        assertEquals(validRequest().lastName, responseBody?.lastName)
        assertEquals(validRequest().specialty, responseBody?.specialty)
        assertEquals(validRequest().practiceStartDate, responseBody?.practiceStartDate)
        assertNotNull(responseBody?.yearsOfExperience)
    }

    @Test
    fun `should return 400 when npiNumber is invalid`() {
        val invalidRequest = DoctorRegistrationRequest(
            firstName = "Jane",
            lastName = "Smith",
            npiNumber = "123",
            specialty = "ORTHO",
            practiceStartDate = "01/15/2020"
        )

        val request = HttpRequest.POST("/api/doctors", invalidRequest)
            .contentType(MediaType.APPLICATION_JSON)

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(request, Any::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `should return 400 when firstName is missing`() {
        val invalidJson = mapOf(
            "lastName" to "Smith",
            "npiNumber" to "1234567890",
            "specialty" to "CARDIO",
            "practiceStartDate" to "01/15/2020"
        )

        val request = HttpRequest.POST("/api/doctors", invalidJson)
            .contentType(MediaType.APPLICATION_JSON)

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(request, Any::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `should return 400 when practiceStartDate format is invalid`() {
        val invalidRequest = DoctorRegistrationRequest(
            firstName = "Jane",
            lastName = "Smith",
            npiNumber = "1234567890",
            specialty = "ORTHO",
            practiceStartDate = "2020-01-15"
        )

        val request = HttpRequest.POST("/api/doctors", invalidRequest)
            .contentType(MediaType.APPLICATION_JSON)

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(request, Any::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }
}