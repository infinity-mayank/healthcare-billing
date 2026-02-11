package com.billing.controller

import com.billing.TestDatabaseCleaner
import com.billing.dto.DoctorRegistrationRequest
import com.billing.dto.DoctorResponse
import io.micronaut.core.type.Argument
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
import kotlin.collections.find

@MicronautTest
class DoctorControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var dbCleaner: TestDatabaseCleaner


    private fun validRequest() =
        DoctorRegistrationRequest(
            firstName = "Jane",
            lastName = "Smith",
            npiNumber = "1234567890",
            specialty = "CARDIO",
            practiceStartDate = "01/15/2020"
        )

    @BeforeEach
    fun setup() {
        dbCleaner.clean("doctors")
    }

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

    @Test
    fun `should get all doctors and return 200`() {
        val doctor1 = DoctorRegistrationRequest(
            firstName = "Jane",
            lastName = "Smith",
            npiNumber = "1234567890",
            specialty = "CARDIO",
            practiceStartDate = "01/15/2020"
        )
        val createRequest1 = HttpRequest.POST("/api/doctors", doctor1)
            .contentType(MediaType.APPLICATION_JSON)
        client.toBlocking().exchange(createRequest1, DoctorResponse::class.java)

        val doctor2 = DoctorRegistrationRequest(
            firstName = "John",
            lastName = "Doe",
            npiNumber = "1234567891",
            specialty = "ORTHO",
            practiceStartDate = "01/15/2015"
        )
        val createRequest2 = HttpRequest.POST("/api/doctors", doctor2)
            .contentType(MediaType.APPLICATION_JSON)
        client.toBlocking().exchange(createRequest2, DoctorResponse::class.java)

        val getRequest = HttpRequest.GET<Any>("/api/doctors")
        val response = client.toBlocking()
            .exchange(getRequest, Argument.listOf(DoctorResponse::class.java))

        assertEquals(HttpStatus.OK, response.status)

        val doctors = response.body()
        assertNotNull(doctors)
        assertTrue(doctors!!.size >= 2, "Expected at least 2 doctors in the response")

        val johnDoe = doctors.find { it.firstName == "John" && it.lastName == "Doe" }
        assertNotNull(johnDoe)
        assertEquals("01/15/2015", johnDoe!!.practiceStartDate)
        assertEquals("1234567891", johnDoe.npiNumber)
        assertEquals("ORTHO", johnDoe.specialty)

        val janeSmith = doctors.find { it.firstName == "Jane" && it.lastName == "Smith" }
        assertNotNull(janeSmith)
        assertEquals("01/15/2020", janeSmith!!.practiceStartDate)
        assertEquals("1234567890", janeSmith.npiNumber)
        assertEquals("CARDIO", janeSmith.specialty)
    }
}