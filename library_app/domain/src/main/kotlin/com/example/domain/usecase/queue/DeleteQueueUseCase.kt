package com.example.domain.usecase.queue

import com.example.domain.repository.QueueRepository
import java.util.UUID

class DeleteQueueUseCase(
    private val queueRepository: QueueRepository
) {
    suspend operator fun invoke(queueId: UUID) {
        queueRepository.deleteById(queueId)
    }
}