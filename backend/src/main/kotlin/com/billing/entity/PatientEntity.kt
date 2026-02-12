package com.billing.entity

import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@MappedEntity("patients")
data class PatientEntity(
    @field:Id
    val id: UUID = UUID.randomUUID(),
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val insuranceBIN: String,
    val insurancePCN: String,
    val insuranceMemberID: String,
    @DateCreated
    val createdAt: LocalDateTime? = null,
    @DateUpdated
    val updatedAt: LocalDateTime? = null
)

