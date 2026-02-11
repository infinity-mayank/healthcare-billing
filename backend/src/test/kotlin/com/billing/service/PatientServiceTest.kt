package com.billing.service

import com.billing.dto.PatientRegistrationRequest
import com.billing.entity.PatientEntity
import com.billing.repository.PatientRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate

class PatientServiceTest {

    private val patientRepository = mock<PatientRepository>()
    private val patientService = PatientService(patientRepository)

    @Test
    fun `should register patient and return response`() {
        val request = PatientRegistrationRequest(
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = "01/01/1990",
            insuranceBIN = "123456",
            insurancePCN = "349834",
            insuranceMemberID = "398985"
        )

        whenever(patientRepository.save(any()))
            .thenAnswer { it.arguments[0] }

        val response = patientService.registerPatient(request)

        assertEquals("John", response.firstName)
        assertEquals("Doe", response.lastName)
        assertEquals("123456", response.insuranceBIN)
        assertEquals("349834", response.insurancePCN)
        assertEquals("398985", response.insuranceMemberID)
    }

    @Test
    fun `should get all patients and return list of responses`() {
        val patient1 = PatientEntity(
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = LocalDate.of(1990, 1, 1),
            insuranceBIN = "123456",
            insurancePCN = "376833",
            insuranceMemberID = "398733"
        )

        val patient2 = PatientEntity(
            firstName = "Jane",
            lastName = "Smith",
            dateOfBirth = LocalDate.of(1985, 5, 15),
            insuranceBIN = "654321",
            insurancePCN = "398466",
            insuranceMemberID = "985983"
        )

        whenever(patientRepository.findAll())
            .thenReturn(listOf(patient1, patient2))

        val responses = patientService.getAllPatients()

        assertEquals(2, responses.size)

        val response1 = responses.find { it.firstName == "John" && it.lastName == "Doe" }
        assertEquals("01/01/1990", response1?.dateOfBirth)
        assertEquals("123456", response1?.insuranceBIN)
        assertEquals("376833", response1?.insurancePCN)
        assertEquals("398733", response1?.insuranceMemberID)

        val response2 = responses.find { it.firstName == "Jane" && it.lastName == "Smith" }
        assertEquals("05/15/1985", response2?.dateOfBirth)
        assertEquals("654321", response2?.insuranceBIN)
        assertEquals("398466", response2?.insurancePCN)
        assertEquals("985983", response2?.insuranceMemberID)
    }
}
