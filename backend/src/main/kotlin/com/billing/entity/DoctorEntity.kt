package com.billing.entity

import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.time.LocalDate

@MappedEntity("doctors")
data class DoctorEntity(
    @field:Id
    val npiNumber: String,
    val firstName: String,
    val lastName: String,
    val specialty: String,
    val practiceStartDate: LocalDate
)

