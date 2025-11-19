package com.example.data.local.mapping

import com.example.data.local.entity.IssuanceEntity
import com.example.domain.model.IssuanceModel
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

object IssuanceMapper {
    fun toDomain(row: ResultRow): IssuanceModel {
        return IssuanceModel(
            id = row[IssuanceEntity.id].value,
            bookId = row[IssuanceEntity.bookId].value,
            userId = row[IssuanceEntity.userId].value,
            issuanceDate = row[IssuanceEntity.issuanceDate].toJavaLocalDate(),
            returnDate = row[IssuanceEntity.returnDate].toJavaLocalDate(),
        )
    }

    fun toInsertStatement(
        issuanceModel: IssuanceModel,
        statement: InsertStatement<EntityID<UUID>>
    ): InsertStatement<EntityID<UUID>> {
        return statement.also {
            it[IssuanceEntity.id] = issuanceModel.id
            it[IssuanceEntity.bookId] = issuanceModel.bookId
            it[IssuanceEntity.userId] = issuanceModel.userId
            it[IssuanceEntity.issuanceDate] = issuanceModel.issuanceDate.toKotlinLocalDate()
            it[IssuanceEntity.returnDate] = issuanceModel.returnDate.toKotlinLocalDate()
        }
    }

    fun toUpdateStatement(
        issuanceModel: IssuanceModel,
        statement: UpdateStatement
    ): UpdateStatement {
        return statement.also {
            it[IssuanceEntity.id] = issuanceModel.id
            it[IssuanceEntity.bookId] = issuanceModel.bookId
            it[IssuanceEntity.userId] = issuanceModel.userId
            it[IssuanceEntity.issuanceDate] = issuanceModel.issuanceDate.toKotlinLocalDate()
            it[IssuanceEntity.returnDate] = issuanceModel.returnDate.toKotlinLocalDate()
        }
    }
}