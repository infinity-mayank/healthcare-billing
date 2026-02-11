package com.billing

import jakarta.inject.Singleton
import javax.sql.DataSource

@Singleton
class TestDatabaseCleaner(
    private val dataSource: DataSource
) {

    fun clean(vararg tables: String) {
        dataSource.connection.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute("SET REFERENTIAL_INTEGRITY FALSE")

                tables.forEach { table ->
                    stmt.execute("DELETE FROM $table")
                }

                stmt.execute("SET REFERENTIAL_INTEGRITY TRUE")
            }
        }
    }
}
