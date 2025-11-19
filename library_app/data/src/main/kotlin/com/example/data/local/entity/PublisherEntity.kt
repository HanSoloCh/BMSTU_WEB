package com.example.data.local.entity

import org.jetbrains.exposed.dao.id.UUIDTable

object PublisherEntity : UUIDTable("publisher") {
    val name = varchar("name", 255)
    val description = varchar("description", 255).nullable()
    val email = varchar("email", 50).nullable()
    val phoneNumber = varchar("phone_number", 20).nullable()
}

