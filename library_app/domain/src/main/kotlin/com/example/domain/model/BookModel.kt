package com.example.domain.model

import com.example.domain.exception.EmptyStringException
import com.example.domain.exception.InvalidDateException
import com.example.domain.exception.InvalidValueException
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Year
import java.util.UUID

@Serializable
data class BookModel(
    val id: @Contextual UUID = UUID.randomUUID(),
    val title: String,
    val annotation: String? = null,
    val authors: List<@Contextual UUID>,
    val publisherId: @Contextual UUID? = null,
    val publicationYear: Int? = null,
    val codeISBN: String? = null,
    val bbkId: @Contextual UUID,
    val mediaType: String? = null,
    val volume: String? = null,
    val language: String? = null,
    val originalLanguage: String? = null,
    val copies: Int = 0,
    val availableCopies: Int = 0,
) {
    init {
        when {
            title.isBlank() -> throw EmptyStringException("title")
            annotation != null && annotation.isBlank() -> throw EmptyStringException("annotation")
            publicationYear != null && publicationYear !in 0..Year.now().value -> throw InvalidDateException(
                publicationYear.toString()
            )

            mediaType != null && mediaType.isBlank() -> throw EmptyStringException("mediaType")
            volume != null && volume.isBlank() -> throw EmptyStringException("volume")
            language != null && language.isBlank() -> throw EmptyStringException("language")
            originalLanguage != null && originalLanguage.isBlank() -> throw EmptyStringException("originalLanguage")
            copies < 0 -> throw InvalidValueException("copies", copies.toString())
            availableCopies !in 0..copies -> throw InvalidValueException(
                "availableCopies",
                availableCopies.toString()
            )
        }
    }
}