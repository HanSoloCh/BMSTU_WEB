package com.example.domain.usecase.reservation

import com.example.domain.repository.ReservationRepository
import java.util.UUID

class DeleteReservationUseCase(
    private val reservationRepository: ReservationRepository
) {
    suspend operator fun invoke(reservationId: UUID) {
        reservationRepository.deleteById(reservationId)
    }
}