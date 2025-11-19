package com.example.data.e2e

import com.example.data.local.entity.ApuEntity
import com.example.data.local.entity.AuthorEntity
import com.example.data.local.entity.BbkEntity
import com.example.data.local.entity.BookAuthorCrossRef
import com.example.data.local.entity.BookEntity
import com.example.data.local.entity.IssuanceEntity
import com.example.data.local.entity.PublisherEntity
import com.example.data.local.entity.ReservationEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.UserFavoriteCrossRef
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.HoconApplicationConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.PostgreSQLContainer

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseE2ETest {

    protected lateinit var container: PostgreSQLContainer<Nothing>
    protected lateinit var db: Database
    protected lateinit var testConfig: ApplicationConfig

    private lateinit var mode: String

    @BeforeAll
    fun setupDatabase() {
        val config = ConfigFactory.load("application-test.conf")
        mode = config.getString("test.database.mode")

        if (mode == "container") {
            container = PostgreSQLContainer<Nothing>("postgres:16").apply {
                withDatabaseName("testdb")
                withUsername("testuser")
                withPassword("testpass")
                start()
            }

            testConfig = HoconApplicationConfig(
                ConfigFactory.parseMap(
                    mapOf(
                        "ktor.database.driver" to "org.postgresql.Driver",
                        "ktor.database.url" to container.jdbcUrl,
                        "ktor.database.user" to container.username,
                        "ktor.database.password" to container.password,
                        "ktor.database.maxPoolSize" to 5
                    )
                )
            )
        } else if (mode == "external") {
            testConfig = HoconApplicationConfig(config)
        } else {
            error("Unknown db mode: $mode")
        }

        // Подключаемся к базе
        db = Database.connect(
            url = testConfig.property("ktor.database.url").getString(),
            driver = testConfig.property("ktor.database.driver").getString(),
            user = testConfig.property("ktor.database.user").getString(),
            password = testConfig.property("ktor.database.password").getString()
        )

        // Создаём таблицы один раз
        transaction(db) {
            SchemaUtils.createMissingTablesAndColumns(
                ApuEntity,
                AuthorEntity,
                BbkEntity,
                BookAuthorCrossRef,
                BookEntity,
                IssuanceEntity,
                PublisherEntity,
                ReservationEntity,
                UserEntity,
                UserFavoriteCrossRef
            )
        }
    }

    @AfterAll
    fun teardownDatabase() {
        if (mode == "container") {
            container.stop()
        }
    }

    @BeforeEach
    fun cleanDatabase() {
        transaction(db) {
            ReservationEntity.deleteAll()
            IssuanceEntity.deleteAll()
            UserFavoriteCrossRef.deleteAll()
            UserEntity.deleteAll()
            AuthorEntity.deleteAll()
            BookEntity.deleteAll()
            BookAuthorCrossRef.deleteAll()
            PublisherEntity.deleteAll()
            ApuEntity.deleteAll()
            BbkEntity.deleteAll()
        }
    }
}
