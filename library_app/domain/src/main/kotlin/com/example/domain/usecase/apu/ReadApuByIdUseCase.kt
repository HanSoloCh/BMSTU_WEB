package com.example.domain.usecase.apu

import com.example.domain.model.ApuModel
import com.example.domain.repository.ApuRepository
import java.util.UUID

class ReadApuByIdUseCase(
    private val apuRepository: ApuRepository
) {
    suspend operator fun invoke(apuId: UUID): ApuModel? {
        return apuRepository.readById(apuId)
    }
}