package com.example.data.local.entity

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object BookAuthorCrossRef : Table("book_author") {
    val bookId = reference("book_id", BookEntity, onDelete = ReferenceOption.CASCADE)
    val authorId = reference("author_id", AuthorEntity, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(bookId, authorId)
}
