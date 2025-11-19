package com.example.data.local.mapping

import com.example.data.local.entity.BbkEntity
import com.example.domain.model.BbkModel
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

object BbkMapper {
    fun toDomain(row: ResultRow): BbkModel {
        return BbkModel(
            id = row[BbkEntity.id].value,
            code = row[BbkEntity.code],
            description = row[BbkEntity.description],
        )
    }

    fun toInsertStatement(
        bbkModel: BbkModel,
        statement: InsertStatement<EntityID<UUID>>
    ): InsertStatement<EntityID<UUID>> {
        return statement.also {
            it[BbkEntity.id] = bbkModel.id
            it[BbkEntity.code] = bbkModel.code
            it[BbkEntity.description] = bbkModel.description
        }
    }

    fun toUpdateStatement(bbkModel: BbkModel, statement: UpdateStatement): UpdateStatement {
        return statement.also {
            it[BbkEntity.id] = bbkModel.id
            it[BbkEntity.code] = bbkModel.code
            it[BbkEntity.description] = bbkModel.description
        }
    }
}