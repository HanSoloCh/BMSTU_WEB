package com.example.domain.usecase.reservation

import com.example.domain.exception.InvalidValueException
import com.example.domain.model.ReservationModel
import com.example.domain.repository.ReservationRepository
import com.example.domain.specification.AndSpecification
import com.example.domain.specification.reservation.ReservationBookIdSpecification
import com.example.domain.specification.reservation.ReservationUserIdSpecification
import java.util.UUID

class ReadReservationUseCase(
    private val reservationRepository: ReservationRepository
) {
    suspend operator fun invoke(
        bookId: UUID?,
        userId: UUID?,
        page: Int = 0,
        pageSize: Int = 20
    ): List<ReservationModel> {
        if (bookId != null && userId != null)
            return reservationRepository.query(
                AndSpecification(
                    listOf(
                        ReservationBookIdSpecification(bookId),
                        ReservationUserIdSpecification(userId)
                    )
                ),
                page,
                pageSize
            )
        else if (bookId != null)
            return reservationRepository.query(
                ReservationBookIdSpecification(bookId),
                page,
                pageSize
            )
        else if (userId != null)
            return reservationRepository.query(
                ReservationUserIdSpecification(userId),
                page,
                pageSize
            )
        else
            throw InvalidValueException("bookId, userId", "null, null")
    }
}