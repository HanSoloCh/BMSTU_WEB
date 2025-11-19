package com.example.domain.repository

import com.example.domain.model.BbkModel
import com.example.domain.specification.Specification
import java.util.UUID

interface BbkRepository {
    suspend fun readById(bbkId: UUID): BbkModel?

    suspend fun create(bbkModel: BbkModel): UUID

    suspend fun update(bbkModel: BbkModel): Int

    suspend fun deleteById(bbkId: UUID): Int

    suspend fun isContain(spec: Specification<BbkModel>): Boolean

    suspend fun query(
        spec: Specification<BbkModel>,
        page: Int = 0,
        pageSize: Int = 20
    ): List<BbkModel>
}