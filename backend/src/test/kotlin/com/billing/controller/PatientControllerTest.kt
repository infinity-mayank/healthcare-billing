package com.billing.controller

import com.billing.dto.PatientRegistrationRequest
import com.billing.dto.PatientResponse
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
class PatientControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

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

    @Test
    fun `should get all patients and return 200`() {
        val patient1 = PatientRegistrationRequest(
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = "01/01/1990",
            insuranceBIN = "123456",
            insurancePCN = "999888",
            insuranceMemberID = "2229838"
        )
        val createRequest1 = HttpRequest.POST("/api/patients", patient1)
            .contentType(MediaType.APPLICATION_JSON)
        client.toBlocking().exchange(createRequest1, PatientResponse::class.java)

        val patient2 = PatientRegistrationRequest(
            firstName = "Jane",
            lastName = "Smith",
            dateOfBirth = "05/15/1985",
            insuranceBIN = "654321",
            insurancePCN = "888999",
            insuranceMemberID = "3338282"
        )
        val createRequest2 = HttpRequest.POST("/api/patients", patient2)
            .contentType(MediaType.APPLICATION_JSON)
        client.toBlocking().exchange(createRequest2, PatientResponse::class.java)

        val getRequest = HttpRequest.GET<Any>("/api/patients")
        val response = client.toBlocking()
            .exchange(getRequest, io.micronaut.core.type.Argument.listOf(PatientResponse::class.java))

        assertEquals(HttpStatus.OK, response.status)

        val patients = response.body()
        assertNotNull(patients)
        assertTrue(patients!!.size >= 2, "Expected at least 2 patients in the response")

        val johnDoe = patients.find { it.firstName == "John" && it.lastName == "Doe" }
        assertNotNull(johnDoe)
        assertEquals("01/01/1990", johnDoe!!.dateOfBirth)
        assertEquals("123456", johnDoe.insuranceBIN)
        assertEquals("999888", johnDoe.insurancePCN)
        assertEquals("2229838", johnDoe.insuranceMemberID)

        val janeSmith = patients.find { it.firstName == "Jane" && it.lastName == "Smith" }
        assertNotNull(janeSmith)
        assertEquals("05/15/1985", janeSmith!!.dateOfBirth)
        assertEquals("654321", janeSmith.insuranceBIN)
        assertEquals("888999", janeSmith.insurancePCN)
        assertEquals("3338282", janeSmith.insuranceMemberID)
    }
}
