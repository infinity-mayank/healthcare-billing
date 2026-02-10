package com.billing.model

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

data class Doctor(
    val firstName: String,
    val lastName: String,
    val npiNumber: String,
    val specialty: Specialty,
    val practiceStartDate: LocalDate
) {
    fun calculateYearsOfExperience(): Int {
        return Period.between(practiceStartDate, LocalDate.now()).years
    }

    fun getFormattedPracticeStartDate(): String {
        return practiceStartDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
    }

    companion object {
        fun parsePracticeStartDate(dateString: String): LocalDate {
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            return LocalDate.parse(dateString, formatter)
        }
    }
}

enum class Specialty(
    val displayName: String,
    val feeUnder20Years: Double,
    val fee20to30Years: Double,
    val feeOver30Years: Double
) {
    ORTHO("Orthopedics", 800.0, 1000.0, 1500.0),
    CARDIO("Cardiology", 1000.0, 1500.0, 2000.0);

    companion object {
        fun fromString(value: String): Specialty? {
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}
