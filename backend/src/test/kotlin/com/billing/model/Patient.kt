package com.billing.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class PatientTest {

    @Test
    fun `should format date of birth correctly`() {
        val patient = Patient(
            id = "P0001",
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = LocalDate.of(1990, 1, 5),
            insurance = Insurance(
                binNumber = "123456",
                pcnNumber = "PCN001",
                memberId = "MID123"
            )
        )

        val formattedDob = patient.getFormattedDOB()

        assertEquals("01/05/1990", formattedDob)
    }

    @Test
    fun `should parse date of birth correctly`() {
        val date = Patient.parseDateOfBirth("12/25/1995")

        assertEquals(LocalDate.of(1995, 12, 25), date)
    }
}
