package com.billing.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Patient(
    val id: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val insurance: Insurance
) {
    fun getFormattedDOB(): String {
        return dateOfBirth.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
    }

    companion object {
        fun parseDateOfBirth(dateString: String): LocalDate {
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            return LocalDate.parse(dateString, formatter)
        }
    }
}


data class Insurance(
    val binNumber: String,
    val pcnNumber: String,
    val memberId: String
)
