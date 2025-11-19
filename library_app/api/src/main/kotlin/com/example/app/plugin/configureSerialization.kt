package com.example.app.plugin

import com.example.app.serializer.InstantSerializer
import com.example.app.serializer.LocalDateSerializer
import com.example.app.serializer.UUIDSerializer
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            serializersModule = SerializersModule {
                contextual(UUID::class, UUIDSerializer)
                contextual(LocalDate::class, LocalDateSerializer)
                contextual(Instant::class, InstantSerializer)
            }
            ignoreUnknownKeys = true
            encodeDefaults = true
        })
    }
}