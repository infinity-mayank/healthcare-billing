package com.billing.repository

import com.billing.entity.DoctorEntity
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.H2)
interface DoctorRepository : CrudRepository<DoctorEntity, String> {}
