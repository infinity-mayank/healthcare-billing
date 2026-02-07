package com.billing.service

import com.billing.dto.PatientRegistrationRequest
import com.billing.repository.PatientRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

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
            insurancePCN = "PCN001",
            insuranceMemberID = "MID123"
        )

        whenever(patientRepository.save(any()))
            .thenAnswer { it.arguments[0] }

        val response = patientService.registerPatient(request)

        assertEquals("John", response.firstName)
        assertEquals("Doe", response.lastName)
        assertEquals("123456", response.insuranceBIN)
        assertEquals("PCN001", response.insurancePCN)
        assertEquals("MID123", response.insuranceMemberID)
    }
}
