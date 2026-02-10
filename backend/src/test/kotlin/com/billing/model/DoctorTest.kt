package com.billing.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DoctorTest {

    @Test
    fun `should format practice start date correctly`() {
        val doctor = Doctor(
            firstName = "Jane",
            lastName = "Smith",
            npiNumber = "1234567890",
            specialty = Specialty.ORTHO,
            practiceStartDate = LocalDate.of(2010, 3, 15)
        )

        val formattedDate = doctor.getFormattedPracticeStartDate()

        assertEquals("03/15/2010", formattedDate)
    }

    @Test
    fun `should parse practice start date correctly`() {
        val date = Doctor.parsePracticeStartDate("06/20/2015")

        assertEquals(LocalDate.of(2015, 6, 20), date)
    }

    @Test
    fun `should calculate years of experience correctly`() {
        val doctor = Doctor(
            firstName = "Jane",
            lastName = "Smith",
            npiNumber = "1234567890",
            specialty = Specialty.CARDIO,
            practiceStartDate = LocalDate.of(2010, 2, 10)
        )

        val yearsOfExperience = doctor.calculateYearsOfExperience()

        assertEquals(16, yearsOfExperience)
    }

    @Test
    fun `should parse specialty from string`() {
        val specialty = Specialty.fromString("ORTHO")

        assertEquals(Specialty.ORTHO, specialty)
    }
}
