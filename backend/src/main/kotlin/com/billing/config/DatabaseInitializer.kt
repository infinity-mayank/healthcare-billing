package com.billing.config

import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.event.ApplicationStartupEvent
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import javax.sql.DataSource

@Singleton
@Transactional
class DatabaseInitializer(
    private val dataSource: DataSource
) : ApplicationEventListener<ApplicationStartupEvent> {

    private val logger = LoggerFactory.getLogger(DatabaseInitializer::class.java)

    override fun onApplicationEvent(event: ApplicationStartupEvent) {
        logger.info("Initializing database schema")

        val sqlStatements = loadSchemaStatements()
        executeStatements(sqlStatements)

        logger.info("Database schema initialized successfully")
    }

    private fun loadSchemaStatements(): List<String> =
        this::class.java.classLoader
            .getResourceAsStream("schema.sql")
            ?.bufferedReader()
            ?.use { it.readText() }
            ?.splitToSequence(";")
            ?.map(String::trim)
            ?.filter(String::isNotEmpty)
            ?.toList()
            ?: throw IllegalStateException("schema.sql not found")

    private fun executeStatements(statements: List<String>) {
        dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statements.forEach { sql ->
                    logger.debug("Executing SQL: {}", sql)
                    statement.execute(sql)
                }
            }
        }
    }
}
