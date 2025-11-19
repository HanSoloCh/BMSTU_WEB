package com.example.domain.usecase.bbk

import com.example.domain.exception.ModelDuplicateException
import com.example.domain.model.BbkModel
import com.example.domain.repository.BbkRepository
import com.example.domain.specification.bbk.BbkIdSpecification
import java.util.UUID

class CreateBbkUseCase(
    private val bbkRepository: BbkRepository
) {
    suspend operator fun invoke(bbkModel: BbkModel): UUID {
        if (bbkRepository.isContain(BbkIdSpecification(bbkModel.id)))
            throw ModelDuplicateException("Bbk", bbkModel.id)

        return bbkRepository.create(bbkModel)
    }
}