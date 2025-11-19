package com.example.data.local.mapping

import com.example.data.local.entity.PublisherEntity
import com.example.domain.model.PublisherModel
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

object PublisherMapper {
    fun toDomain(row: ResultRow): PublisherModel {
        return PublisherModel(
            id = row[PublisherEntity.id].value,
            name = row[PublisherEntity.name],
            description = row[PublisherEntity.description],
            email = row[PublisherEntity.email],
            phoneNumber = row[PublisherEntity.phoneNumber]
        )
    }

    fun toInsertStatement(
        publisherModel: PublisherModel,
        statement: InsertStatement<EntityID<UUID>>
    ): InsertStatement<EntityID<UUID>> {
        return statement.also {
            it[PublisherEntity.id] = publisherModel.id
            it[PublisherEntity.name] = publisherModel.name
            it[PublisherEntity.description] = publisherModel.description
            it[PublisherEntity.email] = publisherModel.email
            it[PublisherEntity.phoneNumber] = publisherModel.phoneNumber
        }
    }

    fun toUpdateStatement(
        publisherModel: PublisherModel,
        statement: UpdateStatement
    ): UpdateStatement {
        return statement.also {
            it[PublisherEntity.id] = publisherModel.id
            it[PublisherEntity.name] = publisherModel.name
            it[PublisherEntity.description] = publisherModel.description
            it[PublisherEntity.email] = publisherModel.email
            it[PublisherEntity.phoneNumber] = publisherModel.phoneNumber
        }
    }
}