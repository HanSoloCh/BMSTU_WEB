package com.example.domain.usecase.apu

import com.example.domain.model.ApuModel
import com.example.domain.repository.ApuRepository
import com.example.domain.specification.apu.ApuTermSpecification

class ReadApuByTermUseCase(
    private val apuRepository: ApuRepository
) {
    suspend operator fun invoke(
        apuTerm: String,
        page: Int = 0,
        pageSize: Int = 20
    ): List<ApuModel> {
        return apuRepository.query(ApuTermSpecification(apuTerm), page, pageSize)
    }
}

