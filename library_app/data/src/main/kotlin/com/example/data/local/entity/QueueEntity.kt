package com.example.data.local.entity

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object QueueEntity : UUIDTable("queue") {
    val bookId = reference("book_id", BookEntity, onDelete = ReferenceOption.CASCADE)
    val userId = reference("user_id", UserEntity, onDelete = ReferenceOption.CASCADE)
    val createdAt = timestamp("created_at")
}
