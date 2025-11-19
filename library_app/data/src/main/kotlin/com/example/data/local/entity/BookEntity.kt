package com.example.data.local.entity

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object BookEntity : UUIDTable("book") {
    val title = text("title")
    val annotation = text("annotation").nullable()
    val publisherId =
        reference("publisher_id", PublisherEntity, onDelete = ReferenceOption.CASCADE).nullable()
    val publicationYear = integer("publication_year").nullable()
    val codeISBN = varchar("ISBN", 50).nullable()
    val bbkId = reference("bbk_id", BbkEntity, onDelete = ReferenceOption.RESTRICT)
    val mediaType = varchar("media_type", 100).nullable()
    val volume = text("volume").nullable()
    val language = varchar("language", 100).nullable()
    val originalLanguage = varchar("original_language", 100).nullable()
    val copies = integer("copies").default(0)
    val availableCopies = integer("available_copies").default(0)
}