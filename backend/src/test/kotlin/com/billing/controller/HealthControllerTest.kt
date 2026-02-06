package com.billing.controller

import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@MicronautTest
class HealthControllerTest {

    @Inject
    @field:Client("/api")
    lateinit var httpClient: HttpClient

    @Test
    fun testHealthEndpoint() {
        val response = httpClient.toBlocking().exchange("/health", String::class.java)

        assertEquals(HttpStatus.OK, response.status)

        assertEquals("System is running and healthy", response.body())
    }
}
