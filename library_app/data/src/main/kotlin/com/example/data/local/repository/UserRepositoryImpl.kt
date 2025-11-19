package com.example.data.local.repository

import com.example.data.local.entity.UserEntity
import com.example.data.local.mapping.UserMapper
import com.example.data.local.specification.UserSpecToExpressionMapper
import com.example.domain.model.UserModel
import com.example.domain.repository.UserRepository
import com.example.domain.specification.Specification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

class UserRepositoryImpl(
    private val db: Database
) : UserRepository {
    override suspend fun readById(userId: UUID): UserModel? = withContext(Dispatchers.IO) {
        transaction(db) {
            UserEntity.selectAll().where { UserEntity.id eq userId }.firstOrNull()?.let {
                UserMapper.toDomain(it)
            }
        }
    }

    override suspend fun create(userModel: UserModel) = withContext(Dispatchers.IO) {
        transaction(db) {
            UserEntity.insertAndGetId {
                UserMapper.toInsertStatement(userModel, it)
            }.value
        }
    }

    override suspend fun update(userModel: UserModel) = withContext(Dispatchers.IO) {
        transaction(db) {
            UserEntity.update({ UserEntity.id eq userModel.id }) {
                UserMapper.toUpdateStatement(userModel, it)
            }
        }
    }

    override suspend fun deleteById(userId: UUID) = withContext(Dispatchers.IO) {
        transaction(db) {
            UserEntity.deleteWhere { id eq userId }
        }
    }

    override suspend fun isContain(spec: Specification<UserModel>) = withContext(Dispatchers.IO) {
        query(spec).isNotEmpty()
    }

    override suspend fun query(
        spec: Specification<UserModel>,
        page: Int,
        pageSize: Int
    ): List<UserModel> =
        withContext(Dispatchers.IO) {
            val expression = UserSpecToExpressionMapper.map(spec)
            val offset: Long = (page * pageSize).toLong()

            transaction(db) {
                UserEntity.selectAll().where { expression }.limit(pageSize, offset)
                    .map { UserMapper.toDomain(it) }
            }

        }

    override suspend fun login(phone: String, password: String) = withContext(Dispatchers.IO) {
        transaction(db) {
            UserEntity
                .selectAll()
                .where {
                    (UserEntity.phoneNumber eq phone) and (UserEntity.password eq password)
                }
                .firstOrNull()?.let { UserMapper.toDomain(it) }
        }
    }
}