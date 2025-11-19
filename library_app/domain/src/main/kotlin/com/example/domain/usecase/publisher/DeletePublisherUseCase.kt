package com.example.domain.usecase.publisher

import com.example.domain.repository.PublisherRepository
import java.util.UUID

class DeletePublisherUseCase(
    private val publisherRepository: PublisherRepository
) {
    suspend operator fun invoke(publisherId: UUID) {
        publisherRepository.deleteById(publisherId)
    }
}