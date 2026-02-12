package com.billing.dto

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero

@Serdeable
@Introspected
data class BillResponse(
    val patientId: String,
    val doctorNpiNumber: String,
    val consultationFee: Double,
    val taxAmount: Double,
    val totalAmount: Double,
    val coPayAmount: Double,
    val insurancePayableAmount: Double,
    val taxRatePercentage: Double,
    val coPayRatePercentage: Double
)

@Serdeable
@Introspected
data class SaveBillRequest(
    @field:NotBlank(message = "Patient ID is required")
    val patientId: String,

    @field:NotBlank(message = "Doctor NPI number is required")
    val doctorNpiNumber: String,

    @field:Positive(message = "Consultation fee must be positive")
    val consultationFee: Double,

    @field:Positive(message = "Tax amount must be positive")
    val taxAmount: Double,

    @field:Positive(message = "Total amount must be positive")
    val totalAmount: Double,

    @field:Positive(message = "Co-pay amount must be positive")
    val coPayAmount: Double,

    @field:Positive(message = "Insurance payable amount must be positive")
    val insurancePayableAmount: Double,

    @field:PositiveOrZero(message = "Tax rate percentage must be positive or zero")
    val taxRatePercentage: Double,

    @field:PositiveOrZero(message = "Co-pay rate percentage must be positive or zero")
    val coPayRatePercentage: Double
)

@Serdeable
@Introspected
data class SaveBillResponse(
    val id: String,
    val createdAt: String
)

