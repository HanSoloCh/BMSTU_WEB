package com.example.domain.usecase.apu

import com.example.domain.repository.ApuRepository
import java.util.UUID

class DeleteApuUseCase(
    private val apuRepository: ApuRepository
) {
    suspend operator fun invoke(apuInd: UUID) {
        apuRepository.deleteById(apuInd)
    }
}