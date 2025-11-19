package com.example.domain.usecase.publisher

import com.example.domain.exception.ModelNotFoundException
import com.example.domain.model.PublisherModel
import com.example.domain.repository.PublisherRepository
import com.example.domain.specification.publisher.PublisherIdSpecification

class UpdatePublisherUseCase(
    private val publisherRepository: PublisherRepository
) {
    suspend operator fun invoke(publisherModel: PublisherModel) {
        if (!publisherRepository.isContain(PublisherIdSpecification(publisherModel.id)))
            throw ModelNotFoundException("Publisher", publisherModel.id)

        publisherRepository.update(publisherModel)
    }
}