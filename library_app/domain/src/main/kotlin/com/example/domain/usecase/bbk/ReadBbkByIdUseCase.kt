package com.example.domain.usecase.bbk

import com.example.domain.model.BbkModel
import com.example.domain.repository.BbkRepository
import java.util.UUID

class ReadBbkByIdUseCase(
    private val bbkRepository: BbkRepository
) {
    suspend operator fun invoke(bbkId: UUID): BbkModel? {
        return bbkRepository.readById(bbkId)
    }
}
