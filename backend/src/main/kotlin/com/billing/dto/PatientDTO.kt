package com.billing.dto

import com.billing.model.Insurance
import com.billing.model.Patient
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

@Serdeable
@Introspected
data class PatientRegistrationRequest(
    @field:NotBlank(message = "First name is required")
    val firstName: String,

    @field:NotBlank(message = "Last name is required")
    val lastName: String,

    @field:NotBlank(message = "Date of birth is required")
    @field:Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Date of birth must be in MM/DD/YYYY format")
    val dateOfBirth: String,

    @field:NotBlank(message = "Insurance BIN is required")
    @field:Pattern(regexp = "\\d+", message = "Insurance BIN must contain only numbers")
    val insuranceBIN: String,

    @field:NotBlank(message = "Insurance PCN is required")
    @field:Pattern(regexp = "\\d+", message = "Insurance PCN must contain only numbers")
    val insurancePCN: String,

    @field:NotBlank(message = "Insurance Member ID is required")
    @field:Pattern(regexp = "\\d+", message = "Insurance Member ID must contain only numbers")
    val insuranceMemberID: String
) {
    fun toPatient(id: String): Patient {
        return Patient(
            id = id,
            firstName = firstName,
            lastName = lastName,
            dateOfBirth = Patient.parseDateOfBirth(dateOfBirth),
            insurance = Insurance(
                binNumber = insuranceBIN,
                pcnNumber = insurancePCN,
                memberId = insuranceMemberID
            )
        )
    }
}

@Serdeable
@Introspected
data class PatientResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val insuranceBIN: String,
    val insurancePCN: String,
    val insuranceMemberID: String
) {
    companion object {
        /**
         * Convert Patient model to response DTO
         */
        fun fromPatient(patient: Patient): PatientResponse {
            return PatientResponse(
                id = patient.id,
                firstName = patient.firstName,
                lastName = patient.lastName,
                dateOfBirth = patient.getFormattedDOB(),
                insuranceBIN = patient.insurance.binNumber,
                insurancePCN = patient.insurance.pcnNumber,
                insuranceMemberID = patient.insurance.memberId
            )
        }
    }
}
