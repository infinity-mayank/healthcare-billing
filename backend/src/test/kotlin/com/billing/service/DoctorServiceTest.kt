package com.billing.service

import com.billing.dto.DoctorRegistrationRequest
import com.billing.repository.DoctorRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DoctorServiceTest {

    private val doctorRepository = mock<DoctorRepository>()
    private val doctorService = DoctorService(doctorRepository)

    @Test
    fun `should register doctor and return response`() {
        val request = DoctorRegistrationRequest(
            firstName = "Jane",
            lastName = "Smith",
            npiNumber = "1234567890",
            specialty = "CARDIO",
            practiceStartDate = "01/15/2020"
        )

        whenever(doctorRepository.save(any()))
            .thenAnswer { it.arguments[0] }

        val response = doctorService.registerDoctor(request)

        assertEquals("Jane", response.firstName)
        assertEquals("Smith", response.lastName)
        assertEquals("1234567890", response.npiNumber)
        assertEquals("CARDIO", response.specialty)
        assertEquals("01/15/2020", response.practiceStartDate)
    }
}
