package com.billing.model

import org.junit.jupiter.api.Assertions.assertEquals
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

    @Test
    fun `should return correct fee for experience under 20 years`() {
        val orthoFee = Specialty.ORTHO.getFeeByExperience(15)
        val cardioFee = Specialty.CARDIO.getFeeByExperience(15)

        assertEquals(800.0, orthoFee)
        assertEquals(1000.0, cardioFee)
    }

    @Test
    fun `should return correct fee for experience between 20 and 30 years`() {
        val orthoFee = Specialty.ORTHO.getFeeByExperience(25)
        val cardioFee = Specialty.CARDIO.getFeeByExperience(25)

        assertEquals(1000.0, orthoFee)
        assertEquals(1500.0, cardioFee)
    }

    @Test
    fun `should return correct fee for experience over 30 years`() {
        val orthoFee = Specialty.ORTHO.getFeeByExperience(35)
        val cardioFee = Specialty.CARDIO.getFeeByExperience(35)

        assertEquals(1500.0, orthoFee)
        assertEquals(2000.0, cardioFee)
    }
}
