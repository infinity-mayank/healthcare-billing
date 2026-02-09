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
                pcnNumber = "320984",
                memberId = "823873"
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
                pcnNumber = "908403",
                memberId = "387932"
            )
        )

        val saved = repository.save(patient)

        assertEquals("P9999", saved.id)
    }

    @Test
    fun `should return all saved patients`() {
        val patient1 = Patient(
            id = "",
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = LocalDate.of(1990, 1, 1),
            insurance = Insurance(
                binNumber = "123456",
                pcnNumber = "398347",
                memberId = "938934"
            )
        )
        repository.save(patient1)

        val patient2 = Patient(
            id = "",
            firstName = "Jane",
            lastName = "Smith",
            dateOfBirth = LocalDate.of(1985, 5, 15),
            insurance = Insurance(
                binNumber = "654321",
                pcnNumber = "998889",
                memberId = "37439"
            )
        )
        repository.save(patient2)

        val allPatients = repository.findAll()

        assertTrue(allPatients.size == 2, "Expected 2 patients")

        val johnDoe = allPatients.find { it.firstName == "John" && it.lastName == "Doe" }
        assertNotNull(johnDoe)
        assertEquals(LocalDate.of(1990, 1, 1), johnDoe!!.dateOfBirth)
        assertEquals("123456", johnDoe.insurance.binNumber)

        val janeSmith = allPatients.find { it.firstName == "Jane" && it.lastName == "Smith" }
        assertNotNull(janeSmith)
        assertEquals(LocalDate.of(1985, 5, 15), janeSmith!!.dateOfBirth)
        assertEquals("654321", janeSmith.insurance.binNumber)
    }
}
