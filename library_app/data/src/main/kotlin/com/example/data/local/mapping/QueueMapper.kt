package com.example.data.local.mapping

import com.example.data.local.entity.QueueEntity
import com.example.domain.model.QueueModel
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

object QueueMapper {
    fun toDomain(row: ResultRow): QueueModel {
        return QueueModel(
            id = row[QueueEntity.id].value,
            bookId = row[QueueEntity.bookId].value,
            userId = row[QueueEntity.userId].value,
            createdAt = row[QueueEntity.createdAt].toJavaInstant(),
        )
    }

    fun toInsertStatement(
        queueModel: QueueModel,
        statement: InsertStatement<EntityID<UUID>>
    ): InsertStatement<EntityID<UUID>> {
        return statement.also {
            it[QueueEntity.id] = queueModel.id
            it[QueueEntity.bookId] = queueModel.bookId
            it[QueueEntity.userId] = queueModel.userId
            it[QueueEntity.createdAt] = queueModel.createdAt.toKotlinInstant()
        }
    }

    fun toUpdateStatement(
        queueModel: QueueModel,
        statement: UpdateStatement
    ): UpdateStatement {
        return statement.also {
            it[QueueEntity.id] = queueModel.id
            it[QueueEntity.bookId] = queueModel.bookId
            it[QueueEntity.userId] = queueModel.userId
            it[QueueEntity.createdAt] = queueModel.createdAt.toKotlinInstant()
        }
    }

}