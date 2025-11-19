package com.example.domain.repository

import com.example.domain.model.QueueModel
import com.example.domain.specification.Specification
import java.util.UUID

interface QueueRepository {
    suspend fun create(queueModel: QueueModel): UUID

    suspend fun update(queueModel: QueueModel): Int

    suspend fun deleteById(queueId: UUID): Int

    suspend fun isContain(spec: Specification<QueueModel>): Boolean

    suspend fun getQueuePosition(bookId: UUID, userId: UUID): Int?

    suspend fun query(
        spec: Specification<QueueModel>,
        page: Int = 0,
        pageSize: Int = 20
    ): List<QueueModel>
}