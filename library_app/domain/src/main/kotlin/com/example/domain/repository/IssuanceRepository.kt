package com.example.domain.repository

import com.example.domain.model.IssuanceModel
import com.example.domain.specification.Specification
import java.util.UUID

interface IssuanceRepository {
    suspend fun create(issuanceModel: IssuanceModel): UUID

    suspend fun update(issuanceModel: IssuanceModel): Int

    suspend fun deleteById(issuanceId: UUID): Int

    suspend fun isContain(spec: Specification<IssuanceModel>): Boolean

    suspend fun query(
        spec: Specification<IssuanceModel>,
        page: Int = 0,
        pageSize: Int = 20
    ): List<IssuanceModel>
}