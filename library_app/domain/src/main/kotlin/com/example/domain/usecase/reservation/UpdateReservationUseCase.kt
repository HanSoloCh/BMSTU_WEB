package com.example.domain.usecase.reservation

import com.example.domain.exception.ModelNotFoundException
import com.example.domain.model.ReservationModel
import com.example.domain.repository.BookRepository
import com.example.domain.repository.ReservationRepository
import com.example.domain.repository.UserRepository
import com.example.domain.specification.book.BookIdSpecification
import com.example.domain.specification.reservation.ReservationIdSpecification
import com.example.domain.specification.user.UserIdSpecification

class UpdateReservationUseCase(
    private val reservationRepository: ReservationRepository,
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(reservationModel: ReservationModel) {
        if (!reservationRepository.isContain(ReservationIdSpecification(reservationModel.id)))
            throw ModelNotFoundException("Reservation", reservationModel.id)

        if (!userRepository.isContain(UserIdSpecification(reservationModel.userId)))
            throw ModelNotFoundException("User", reservationModel.userId)

        if (!bookRepository.isContain(BookIdSpecification(reservationModel.bookId)))
            throw ModelNotFoundException("Book", reservationModel.bookId)

        reservationRepository.update(reservationModel)
    }
}