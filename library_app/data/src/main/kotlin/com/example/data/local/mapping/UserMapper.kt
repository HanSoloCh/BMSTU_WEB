package com.example.data.local.mapping

import com.example.data.local.entity.UserEntity
import com.example.domain.enums.UserRole
import com.example.domain.model.UserModel
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

object UserMapper {
    fun toDomain(row: ResultRow): UserModel {
        return UserModel(
            id = row[UserEntity.id].value,
            name = row[UserEntity.name],
            surname = row[UserEntity.surname],
            secondName = row[UserEntity.secondName],
            password = row[UserEntity.password],
            phoneNumber = row[UserEntity.phoneNumber],
            email = row[UserEntity.email],
            role = UserRole.valueOf(row[UserEntity.role])
        )
    }

    fun toInsertStatement(
        userModel: UserModel,
        statement: InsertStatement<EntityID<UUID>>
    ): InsertStatement<EntityID<UUID>> {
        return statement.also {
            it[UserEntity.id] = userModel.id
            it[UserEntity.name] = userModel.name
            it[UserEntity.surname] = userModel.surname
            it[UserEntity.secondName] = userModel.secondName
            it[UserEntity.password] = userModel.password
            it[UserEntity.phoneNumber] = userModel.phoneNumber
            it[UserEntity.email] = userModel.email
            it[UserEntity.role] = userModel.role.toString()
        }
    }

    fun toUpdateStatement(userModel: UserModel, statement: UpdateStatement): UpdateStatement {
        return statement.also {
            it[UserEntity.id] = userModel.id
            it[UserEntity.name] = userModel.name
            it[UserEntity.surname] = userModel.surname
            it[UserEntity.secondName] = userModel.secondName
            it[UserEntity.password] = userModel.password
            it[UserEntity.phoneNumber] = userModel.phoneNumber
            it[UserEntity.email] = userModel.email
            it[UserEntity.role] = userModel.role.toString()
        }
    }
}