package com.example.domain.repository

import com.example.domain.model.ApuModel
import com.example.domain.specification.Specification
import java.util.UUID

interface ApuRepository {
    suspend fun readById(apuId: UUID?): ApuModel?

    suspend fun create(apuModel: ApuModel): UUID

    suspend fun update(apuModel: ApuModel): Int

    suspend fun deleteById(apuId: UUID): Int

    suspend fun isContain(spec: Specification<ApuModel>): Boolean

    suspend fun query(
        spec: Specification<ApuModel>,
        page: Int = 0,
        pageSize: Int = 20
    ): List<ApuModel>
}