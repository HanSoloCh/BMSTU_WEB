package com.example.domain.model

import com.example.domain.exception.EmptyStringException
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class AuthorModel(
    val id: @Contextual UUID = UUID.randomUUID(),
    val name: String,
) {
    init {
        when {
            name.isBlank() -> throw EmptyStringException("name")
        }
    }
}
