package com.example.domain.usecase.publisher

import com.example.domain.model.PublisherModel
import com.example.domain.repository.PublisherRepository
import java.util.UUID

class ReadPublisherByIdUseCase(
    private val publisherRepository: PublisherRepository
) {
    suspend operator fun invoke(publisherId: UUID): PublisherModel? {
        return publisherRepository.readById(publisherId)
    }
}
