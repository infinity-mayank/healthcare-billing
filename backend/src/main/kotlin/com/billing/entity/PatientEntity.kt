package com.billing.entity

import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.time.LocalDate
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
    val insuranceMemberID: String
)

