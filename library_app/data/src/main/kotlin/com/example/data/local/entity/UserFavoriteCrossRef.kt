package com.example.data.local.entity

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UserFavoriteCrossRef : Table("user_book") {
    val userId = reference("user_id", UserEntity, onDelete = ReferenceOption.CASCADE)
    val bookId = reference("book_id", BookEntity, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(userId, bookId)
}


