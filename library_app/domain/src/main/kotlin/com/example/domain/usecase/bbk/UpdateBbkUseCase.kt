package com.example.domain.usecase.bbk

import com.example.domain.exception.ModelNotFoundException
import com.example.domain.model.BbkModel
import com.example.domain.repository.BbkRepository
import com.example.domain.specification.bbk.BbkIdSpecification

class UpdateBbkUseCase(
    private val bbkRepository: BbkRepository
) {
    suspend operator fun invoke(bbkModel: BbkModel) {
        if (!bbkRepository.isContain(BbkIdSpecification(bbkModel.id)))
            throw ModelNotFoundException("Bbk", bbkModel.id)

        bbkRepository.update(bbkModel)
    }
}