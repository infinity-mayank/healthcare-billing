package com.billing.service

import com.billing.dto.DoctorRegistrationRequest
import com.billing.model.Doctor
import com.billing.model.Specialty
import com.billing.repository.DoctorRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate

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

    @Test
    fun `should get all doctors and return list of responses`() {
        val doctor1 = Doctor(
            npiNumber = "1234567890",
            firstName = "John",
            lastName = "Doe",
            practiceStartDate = LocalDate.of(1990, 1, 1),
            specialty = Specialty.ORTHO
        )

        val doctor2 = Doctor(
            npiNumber = "1234567891",
            firstName = "Jane",
            lastName = "Smith",
            practiceStartDate = LocalDate.of(1985, 5, 15),
            specialty = Specialty.CARDIO
        )

        whenever(doctorRepository.findAll())
            .thenReturn(listOf(doctor1, doctor2))

        val responses = doctorService.getAllDoctors()

        assertEquals(2, responses.size)

        val response1 = responses.find { it.npiNumber == "1234567890" }
        assertEquals("John", response1?.firstName)
        assertEquals("Doe", response1?.lastName)
        assertEquals("01/01/1990", response1?.practiceStartDate)
        assertEquals("ORTHO", response1?.specialty)

        val response2 = responses.find { it.npiNumber == "1234567891" }
        assertEquals("Jane", response2?.firstName)
        assertEquals("Smith", response2?.lastName)
        assertEquals("05/15/1985", response2?.practiceStartDate)
        assertEquals("CARDIO", response2?.specialty)
    }
}
