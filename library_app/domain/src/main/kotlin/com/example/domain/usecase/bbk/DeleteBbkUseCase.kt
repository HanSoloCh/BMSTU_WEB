package com.example.domain.usecase.bbk

import com.example.domain.repository.BbkRepository
import java.util.UUID

class DeleteBbkUseCase(
    private val bbkRepository: BbkRepository
) {
    suspend operator fun invoke(bbkId: UUID) {
        bbkRepository.deleteById(bbkId)
    }
}