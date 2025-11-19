package com.example.domain.model

import com.example.domain.exception.EmptyStringException
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ApuModel(
    val id: @Contextual UUID = UUID.randomUUID(),
    val term: String,
    val bbkId: @Contextual UUID
) {
    init {
        when {
            term.isBlank() -> throw EmptyStringException("term")
        }
    }
}
