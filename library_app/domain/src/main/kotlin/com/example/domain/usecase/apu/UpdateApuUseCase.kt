package com.example.domain.usecase.apu

import com.example.domain.exception.ModelNotFoundException
import com.example.domain.model.ApuModel
import com.example.domain.repository.ApuRepository
import com.example.domain.repository.BbkRepository
import com.example.domain.specification.apu.ApuIdSpecification
import com.example.domain.specification.bbk.BbkIdSpecification

class UpdateApuUseCase(
    private val apuRepository: ApuRepository,
    private val bbkRepository: BbkRepository
) {
    suspend operator fun invoke(apuModel: ApuModel) {
        if (!apuRepository.isContain(ApuIdSpecification(apuModel.id)))
            throw ModelNotFoundException("Apu", apuModel.id)

        if (!bbkRepository.isContain(BbkIdSpecification(apuModel.bbkId)))
            throw ModelNotFoundException("Bbk", apuModel.bbkId)

        apuRepository.update(apuModel)
    }
}