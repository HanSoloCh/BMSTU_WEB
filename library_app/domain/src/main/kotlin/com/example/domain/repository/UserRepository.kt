package com.example.domain.repository

import com.example.domain.model.UserModel
import com.example.domain.specification.Specification
import java.util.UUID

interface UserRepository {
    suspend fun readById(userId: UUID): UserModel?

    suspend fun create(userModel: UserModel): UUID

    suspend fun update(userModel: UserModel): Int

    suspend fun deleteById(userId: UUID): Int

    suspend fun isContain(spec: Specification<UserModel>): Boolean

    suspend fun query(
        spec: Specification<UserModel>,
        page: Int = 0,
        pageSize: Int = 20
    ): List<UserModel>

    suspend fun login(phone: String, password: String): UserModel?
}