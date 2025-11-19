package com.example.domain.repository

import com.example.domain.model.AuthorModel
import com.example.domain.specification.Specification
import java.util.UUID

interface AuthorRepository {
    suspend fun readById(authorId: UUID): AuthorModel?

    suspend fun create(authorModel: AuthorModel): UUID

    suspend fun update(authorModel: AuthorModel): Int

    suspend fun deleteById(authorId: UUID): Int

    suspend fun isContain(spec: Specification<AuthorModel>): Boolean

    suspend fun query(
        spec: Specification<AuthorModel>,
        page: Int = 0,
        pageSize: Int = 20
    ): List<AuthorModel>
}