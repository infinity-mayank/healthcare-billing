package com.billing.entity

import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.time.LocalDateTime
import java.util.UUID

@MappedEntity("bills")
data class BillEntity(
    @field:Id
    val id: UUID = UUID.randomUUID(),
    val patientId: UUID,
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
    @DateCreated
    val createdAt: LocalDateTime? = null,
    @DateUpdated
    val updatedAt: LocalDateTime? = null
)

