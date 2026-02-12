package com.billing.entity

import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.time.LocalDate
import java.time.LocalDateTime

@MappedEntity("doctors")
data class DoctorEntity(
    @field:Id
    val npiNumber: String,
    val firstName: String,
    val lastName: String,
    val specialty: String,
    val practiceStartDate: LocalDate,
    @DateCreated
    val createdAt: LocalDateTime? = null,
    @DateUpdated
    val updatedAt: LocalDateTime? = null
)

