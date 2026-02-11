package com.billing.repository

import com.billing.entity.PatientEntity
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

@MicronautTest(transactional = false)
class PatientRepositoryTest {

    @Inject
    lateinit var repository: PatientRepository

    @AfterEach
    fun cleanup() {
        repository.deleteAll()
    }

    @Test
    fun `should generate id when patient id is empty`() {
        val patient = PatientEntity(
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = LocalDate.of(1990, 1, 1),
            insuranceBIN = "123456",
            insurancePCN = "320984",
            insuranceMemberID = "823873"
        )

        val saved = repository.save(patient)

        assertNotNull(saved.id)
    }

    @Test
    fun `should return all saved patients`() {
        val patient1 = PatientEntity(
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = LocalDate.of(1990, 1, 1),
            insuranceBIN = "123456",
            insurancePCN = "398347",
            insuranceMemberID = "938934"
        )
        repository.save(patient1)

        val patient2 = PatientEntity(
            firstName = "Jane",
            lastName = "Smith",
            dateOfBirth = LocalDate.of(1985, 5, 15),
            insuranceBIN = "654321",
            insurancePCN = "998889",
            insuranceMemberID = "37439"
        )
        repository.save(patient2)

        val allPatients = repository.findAll()

        assertTrue(allPatients.size >= 2, "Expected at least 2 patients")

        val johnDoe = allPatients.find { it.firstName == "John" && it.lastName == "Doe" }
        assertNotNull(johnDoe)
        assertEquals(LocalDate.of(1990, 1, 1), johnDoe!!.dateOfBirth)
        assertEquals("123456", johnDoe.insuranceBIN)

        val janeSmith = allPatients.find { it.firstName == "Jane" && it.lastName == "Smith" }
        assertNotNull(janeSmith)
        assertEquals(LocalDate.of(1985, 5, 15), janeSmith!!.dateOfBirth)
        assertEquals("654321", janeSmith.insuranceBIN)
    }
}
