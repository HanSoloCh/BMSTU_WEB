package com.example.domain.repository

import com.example.domain.model.PublisherModel
import com.example.domain.specification.Specification
import java.util.UUID

interface PublisherRepository {
    suspend fun readById(publisherId: UUID): PublisherModel?

    suspend fun create(publisherModel: PublisherModel): UUID

    suspend fun update(publisherModel: PublisherModel): Int

    suspend fun deleteById(publisherId: UUID): Int

    suspend fun isContain(spec: Specification<PublisherModel>): Boolean

    suspend fun query(
        spec: Specification<PublisherModel>,
        page: Int = 0,
        pageSize: Int = 20
    ): List<PublisherModel>
}