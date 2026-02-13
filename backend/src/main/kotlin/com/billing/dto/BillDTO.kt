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
    val coPayRatePercentage: Double,
    val discountAmount: Double,
    val discountPercentage: Double,
)

@Serdeable
@Introspected
data class SaveBillRequest(
    @field:NotBlank(message = "Patient ID is required")
    val patientId: String,

    @field:NotBlank(message = "Doctor NPI number is required")
    val doctorNpiNumber: String,
)

@Serdeable
@Introspected
data class SaveBillResponse(
    val id: String,
    val createdAt: String
)

