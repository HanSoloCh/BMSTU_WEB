package com.example.domain.usecase.queue

import com.example.domain.exception.InvalidValueException
import com.example.domain.model.QueueModel
import com.example.domain.repository.QueueRepository
import com.example.domain.specification.AndSpecification
import com.example.domain.specification.queue.QueueBookIdSpecification
import com.example.domain.specification.queue.QueueUserIdSpecification
import java.util.UUID

class ReadQueueUseCase(
    private val queueRepository: QueueRepository
) {
    suspend operator fun invoke(
        bookId: UUID?,
        userId: UUID?,
        page: Int = 0,
        pageSize: Int = 20
    ): List<QueueModel> {
        if (bookId != null && userId != null)
            return queueRepository.query(
                AndSpecification(
                    listOf(
                        QueueBookIdSpecification(bookId),
                        QueueUserIdSpecification(userId)
                    )
                ),
                page,
                pageSize
            )
        else if (bookId != null)
            return queueRepository.query(
                QueueBookIdSpecification(bookId),
                page,
                pageSize
            )
        else if (userId != null)
            return queueRepository.query(QueueUserIdSpecification(userId), page, pageSize)
        else
            throw InvalidValueException("bookId, userId", "null, null")
    }
}