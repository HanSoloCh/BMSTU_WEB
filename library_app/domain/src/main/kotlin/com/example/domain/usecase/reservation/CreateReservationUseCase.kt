package com.example.domain.usecase.reservation

import com.example.domain.exception.BookNoAvailableCopiesException
import com.example.domain.exception.ModelDuplicateException
import com.example.domain.exception.ModelNotFoundException
import com.example.domain.model.ReservationModel
import com.example.domain.repository.BookRepository
import com.example.domain.repository.ReservationRepository
import com.example.domain.repository.UserRepository
import com.example.domain.specification.book.BookIdSpecification
import com.example.domain.specification.reservation.ReservationIdSpecification
import com.example.domain.specification.user.UserIdSpecification
import java.util.UUID

/**
 * Create reservation must decrement available copies
 */
class CreateReservationUseCase(
    private val reservationRepository: ReservationRepository,
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(reservationModel: ReservationModel): UUID {
        if (reservationRepository.isContain(ReservationIdSpecification(reservationModel.id)))
            throw ModelDuplicateException("Reservation", reservationModel.id)

        if (!userRepository.isContain(UserIdSpecification(reservationModel.userId)))
            throw ModelNotFoundException("User", reservationModel.userId)

        if (!bookRepository.isContain(BookIdSpecification(reservationModel.bookId)))
            throw ModelNotFoundException("Book", reservationModel.bookId)

        if (bookRepository.query(BookIdSpecification(reservationModel.bookId))
                .first().availableCopies <= 0
        )
            throw BookNoAvailableCopiesException(reservationModel.bookId)

        return reservationRepository.create(reservationModel)
    }
}