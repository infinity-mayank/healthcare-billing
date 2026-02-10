package com.billing.dto

import com.billing.model.Doctor
import com.billing.model.Specialty
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

@Serdeable
@Introspected
data class DoctorRegistrationRequest(
    @field:NotBlank(message = "First name is required")
    val firstName: String,

    @field:NotBlank(message = "Last name is required")
    val lastName: String,

    @field:NotBlank(message = "NPI number is required")
    @field:Pattern(
        regexp = "^\\d{10}$",
        message = "NPI number must be exactly 10 digits"
    )
    val npiNumber: String,

    @field:NotBlank(message = "Specialty is required")
    val specialty: String,

    @field:NotBlank(message = "Practice start date is required")
    @field:Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Practice start date must be in MM/DD/YYYY format")
    val practiceStartDate: String
) {
    fun toDoctor(): Doctor {
        val specialtyEnum = Specialty.fromString(specialty)
            ?: throw IllegalArgumentException("Invalid specialty: $specialty")

        return Doctor(
            npiNumber = npiNumber,
            firstName = firstName,
            lastName = lastName,
            specialty = specialtyEnum,
            practiceStartDate = Doctor.parsePracticeStartDate(practiceStartDate)
        )
    }
}

@Serdeable
@Introspected
data class DoctorResponse(
    val npiNumber: String,
    val firstName: String,
    val lastName: String,
    val specialty: String,
    val practiceStartDate: String,
    val yearsOfExperience: Int
) {
    companion object {
        fun fromDoctor(doctor: Doctor): DoctorResponse {
            return DoctorResponse(
                npiNumber = doctor.npiNumber,
                firstName = doctor.firstName,
                lastName = doctor.lastName,
                specialty = doctor.specialty.name,
                practiceStartDate = doctor.getFormattedPracticeStartDate(),
                yearsOfExperience = doctor.calculateYearsOfExperience()
            )
        }
    }
}
