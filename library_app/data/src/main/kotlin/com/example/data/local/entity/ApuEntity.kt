package com.example.data.local.entity

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.UUID

object ApuEntity : IdTable<UUID>("apu") {
    override val id = uuid("id").entityId()
    val term = varchar("term", 100)
    val bbkId = reference("bbk_id", BbkEntity, onDelete = ReferenceOption.CASCADE)
}
