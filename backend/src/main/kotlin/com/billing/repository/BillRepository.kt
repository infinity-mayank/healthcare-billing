package com.billing.repository

import com.billing.entity.BillEntity
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.util.UUID

@JdbcRepository(dialect = Dialect.H2)
interface BillRepository : CrudRepository<BillEntity, UUID> {
    fun countByPatientId(patientId: UUID): Long
}

