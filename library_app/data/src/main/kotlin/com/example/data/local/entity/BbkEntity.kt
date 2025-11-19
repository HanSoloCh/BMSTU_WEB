package com.example.data.local.entity

import org.jetbrains.exposed.dao.id.UUIDTable

object BbkEntity : UUIDTable("bbk") {
    val code = varchar("code", 16)
    val description = varchar("description", 255)
}
