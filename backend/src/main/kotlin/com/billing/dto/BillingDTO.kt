package com.billing.dto

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@Introspected
data class BillingResponse(
    val patientId: String,
    val doctorNpiNumber: String,
    val consultationFee: Double,
    val taxAmount: Double,
    val totalAmount: Double,
)

