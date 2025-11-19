package com.example.data.local.entity

import org.jetbrains.exposed.dao.id.UUIDTable

object UserEntity : UUIDTable("user") {
    val name = varchar("name", 50)
    val surname = varchar("surname", 50)
    val secondName = varchar("second_name", 50).nullable()
    val password = varchar("password", 100)
    val phoneNumber = varchar("phone_number", 20)
    val email = varchar("email", 50).nullable()
    val role = varchar("role", 20)
}
