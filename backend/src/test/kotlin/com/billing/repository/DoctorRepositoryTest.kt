package com.billing.repository

import com.billing.model.Doctor
import com.billing.model.Specialty
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DoctorRepositoryTest {

    private val repository = DoctorRepository()

    @Test
    fun `should save doctor with npiNumber as key`() {
        val doctor = Doctor(
            firstName = "John",
            lastName = "Smith",
            npiNumber = "1234567890",
            specialty = Specialty.ORTHO,
            practiceStartDate = LocalDate.of(2000, 1, 1)
        )

        val saved = repository.save(doctor)

        assertEquals("1234567890", saved.npiNumber)
        assertEquals("John", saved.firstName)
        assertEquals("Smith", saved.lastName)
        assertEquals(Specialty.ORTHO, saved.specialty)
        assertEquals(doctor.practiceStartDate, saved.practiceStartDate)
    }
}
