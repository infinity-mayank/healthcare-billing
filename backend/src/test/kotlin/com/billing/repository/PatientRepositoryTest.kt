package com.billing.repository

import com.billing.model.Insurance
import com.billing.model.Patient
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class PatientRepositoryTest {

    private val repository = PatientRepository()

    @Test
    fun `should generate id when patient id is empty`() {
        val patient = Patient(
            id = "",
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = LocalDate.of(1990, 1, 1),
            insurance = Insurance(
                binNumber = "123456",
                pcnNumber = "PCN001",
                memberId = "MID123"
            )
        )

        val saved = repository.save(patient)

        assertTrue(saved.id.startsWith("P"))
        assertEquals(5, saved.id.length)
    }

    @Test
    fun `should keep existing id when patient id is present`() {
        val patient = Patient(
            id = "P9999",
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = LocalDate.of(1990, 1, 1),
            insurance = Insurance(
                binNumber = "123456",
                pcnNumber = "PCN001",
                memberId = "MID123"
            )
        )

        val saved = repository.save(patient)

        assertEquals("P9999", saved.id)
    }
}
