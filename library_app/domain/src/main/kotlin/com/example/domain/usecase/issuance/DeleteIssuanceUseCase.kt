package com.example.domain.usecase.issuance

import com.example.domain.repository.IssuanceRepository
import java.util.UUID

class DeleteIssuanceUseCase(
    private val issuanceRepository: IssuanceRepository
) {
    suspend operator fun invoke(issuanceId: UUID) {
        issuanceRepository.deleteById(issuanceId)
    }
}