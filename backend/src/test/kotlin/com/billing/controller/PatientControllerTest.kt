package com.billing.controller

import com.billing.dto.PatientRegistrationRequest
import com.billing.dto.PatientResponse
import com.billing.service.PatientService
import io.micronaut.core.type.Argument
import io.micronaut.http.*
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock

@MicronautTest
class PatientControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var patientService: PatientService

    @MockBean(PatientService::class)
    fun patientService(): PatientService = mock()

    private fun validRequest() =
        PatientRegistrationRequest(
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = "01/01/1990",
            insuranceBIN = "123456",
            insurancePCN = "999888",
            insuranceMemberID = "2229838"
        )

    @Test
    fun `should create patient and return 201`() {
        val request = HttpRequest.POST("/api/patients", validRequest())
            .contentType(MediaType.APPLICATION_JSON)

        val response = client.toBlocking()
            .exchange(request, PatientResponse::class.java)

        assertEquals(HttpStatus.CREATED, response.status)

        val responseBody = response.body()
        assertNotNull(responseBody?.id)
        assertEquals(validRequest().firstName, responseBody?.firstName)
        assertEquals(validRequest().lastName, responseBody?.lastName)
        assertEquals(validRequest().dateOfBirth, responseBody?.dateOfBirth)
        assertEquals(validRequest().insuranceBIN, responseBody?.insuranceBIN)
        assertEquals(validRequest().insurancePCN, responseBody?.insurancePCN)
        assertEquals(validRequest().insuranceMemberID, responseBody?.insuranceMemberID)
    }

    @Test
    fun `should return 400 when firstName is missing`() {
        val invalidJson = mapOf(
            "lastName" to "Doe",
            "dateOfBirth" to "01/01/1990",
            "insuranceBIN" to "123456",
            "insurancePCN" to "999888",
            "insuranceMemberID" to "294287"
        )

        val request = HttpRequest.POST("/api/patients", invalidJson)
            .contentType(MediaType.APPLICATION_JSON)

        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, Any::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `should return 400 when dateOfBirth format is invalid`() {
        val invalidJson = PatientRegistrationRequest(
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = "1990-01-01",
            insuranceBIN = "123456",
            insurancePCN = "999881",
            insuranceMemberID = "1983748"
        )

        val request = HttpRequest.POST("/api/patients", invalidJson)
            .contentType(MediaType.APPLICATION_JSON)

        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(request, Any::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }
}
