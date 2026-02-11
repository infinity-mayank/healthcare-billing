package com.billing.repository

import com.billing.entity.DoctorEntity
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

@MicronautTest(transactional = false)
class DoctorRepositoryTest {

    @Inject
    lateinit var repository: DoctorRepository

    @AfterEach
    fun cleanup() {
        repository.deleteAll()
    }

    @Test
    fun `should save doctor with npiNumber as key`() {
        val doctor = DoctorEntity(
            firstName = "John",
            lastName = "Smith",
            npiNumber = "1234567890",
            specialty = "ORTHO",
            practiceStartDate = LocalDate.of(2000, 1, 1)
        )

        val saved = repository.save(doctor)

        assertEquals("1234567890", saved.npiNumber)
        assertEquals("John", saved.firstName)
        assertEquals("Smith", saved.lastName)
        assertEquals("ORTHO", saved.specialty)
        assertEquals(doctor.practiceStartDate, saved.practiceStartDate)
    }

    @Test
    fun `should return all saved doctors`() {
        val doctor1 = DoctorEntity(
            npiNumber = "1234567890",
            firstName = "John",
            lastName = "Doe",
            practiceStartDate = LocalDate.of(1990, 1, 1),
            specialty = "CARDIO"
        )
        repository.save(doctor1)

        val doctor2 = DoctorEntity(
            npiNumber = "1234567891",
            firstName = "Jane",
            lastName = "Smith",
            practiceStartDate = LocalDate.of(1985, 5, 15),
            specialty = "ORTHO"
        )
        repository.save(doctor2)

        val allDoctors = repository.findAll()

        assertTrue(allDoctors.size >= 2, "Expected at least 2 doctors")

        val johnDoe = allDoctors.find { it.firstName == "John" && it.lastName == "Doe" }
        assertNotNull(johnDoe)
        assertEquals(LocalDate.of(1990, 1, 1), johnDoe!!.practiceStartDate)
        assertEquals("1234567890", johnDoe.npiNumber)
        assertEquals("CARDIO", johnDoe.specialty)

        val janeSmith = allDoctors.find { it.firstName == "Jane" && it.lastName == "Smith" }
        assertNotNull(janeSmith)
        assertEquals(LocalDate.of(1985, 5, 15), janeSmith!!.practiceStartDate)
        assertEquals("1234567891", janeSmith.npiNumber)
        assertEquals("ORTHO", janeSmith.specialty)
    }

}
