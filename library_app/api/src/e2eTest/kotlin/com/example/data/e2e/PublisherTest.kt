package com.example.data.e2e

import com.example.app.module
import com.example.app.serializer.InstantSerializer
import com.example.app.serializer.LocalDateSerializer
import com.example.app.serializer.UUIDSerializer
import com.example.domain.model.PublisherModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import kotlin.test.assertEquals


class PublisherE2ETest : BaseE2ETest() {

    private val testJson = Json {
        serializersModule = SerializersModule {
            contextual(UUID::class, UUIDSerializer)
            contextual(LocalDate::class, LocalDateSerializer)
            contextual(Instant::class, InstantSerializer)
        }
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    private fun ApplicationTestBuilder.testClient(): HttpClient =
        createClient {
            install(ContentNegotiation) { json(testJson) }
        }

    @Test
    fun `publisher full e2e flow`() = testApplication {
        environment { config = testConfig }
        application { module() }

        val client = testClient()

        // 1. CREATE
        val publisher = PublisherModel(UUID.randomUUID(), "O’Reilly")
        val createResponse = client.post("/publisher") {
            contentType(ContentType.Application.Json)
            setBody(testJson.encodeToString(PublisherModel.serializer(), publisher))
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)

        // 2. READ
        val readResponse = client.get("/publisher/${publisher.id}")
        assertEquals(HttpStatusCode.OK, readResponse.status)
        val created =
            testJson.decodeFromString(PublisherModel.serializer(), readResponse.bodyAsText())
        assertEquals(publisher, created)

        // 3. UPDATE
        val updated = publisher.copy(name = "O’Reilly Media")
        val updateResponse = client.put("/publisher") {
            contentType(ContentType.Application.Json)
            setBody(testJson.encodeToString(PublisherModel.serializer(), updated))
        }
        assertEquals(HttpStatusCode.NoContent, updateResponse.status)

        val afterUpdate = client.get("/publisher/${publisher.id}")
        assertEquals(HttpStatusCode.OK, afterUpdate.status)
        val updatedResult =
            testJson.decodeFromString(PublisherModel.serializer(), afterUpdate.bodyAsText())
        assertEquals("O’Reilly Media", updatedResult.name)

        // 4. DELETE
        val deleteResponse = client.delete("/publisher/${publisher.id}")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        // 5. READ after delete
        val afterDelete = client.get("/publisher/${publisher.id}")
        assertEquals(HttpStatusCode.NotFound, afterDelete.status)
    }
}
