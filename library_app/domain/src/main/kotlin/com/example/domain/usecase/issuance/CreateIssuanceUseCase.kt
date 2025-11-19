package com.example.domain.usecase.issuance

import com.example.domain.exception.BookNoAvailableCopiesException
import com.example.domain.exception.ModelDuplicateException
import com.example.domain.exception.ModelNotFoundException
import com.example.domain.model.BookModel
import com.example.domain.model.IssuanceModel
import com.example.domain.repository.BookRepository
import com.example.domain.repository.IssuanceRepository
import com.example.domain.repository.ReservationRepository
import com.example.domain.repository.UserRepository
import com.example.domain.specification.book.BookIdSpecification
import com.example.domain.specification.issuance.IssuanceIdSpecification
import com.example.domain.specification.reservation.ReservationUserIdSpecification
import com.example.domain.specification.user.UserIdSpecification
import java.util.UUID

class CreateIssuanceUseCase(
    private val issuanceRepository: IssuanceRepository,
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository,
    private val reservationRepository: ReservationRepository
) {
    suspend operator fun invoke(issuanceModel: IssuanceModel): UUID {
        if (issuanceRepository.isContain(IssuanceIdSpecification(issuanceModel.id)))
            throw ModelDuplicateException("Issuance", issuanceModel.id)

        if (!userRepository.isContain(UserIdSpecification(issuanceModel.userId)))
            throw ModelNotFoundException("User", issuanceModel.userId)

        val bookModel = getBookOrThrow(issuanceModel.bookId)

        return createIssuance(issuanceModel, bookModel)
    }

    private suspend fun getBookOrThrow(bookId: UUID): BookModel {
        return bookRepository
            .query(BookIdSpecification(bookId))
            .firstOrNull()
            ?: throw ModelNotFoundException("Book", bookId)
    }

    private suspend fun createIssuance(issuanceModel: IssuanceModel, book: BookModel): UUID {
        val reservations = reservationRepository
            .query(ReservationUserIdSpecification(issuanceModel.userId))

        // Проверить есть ли у пользователя такая бронь на книгу
        val reservation = reservations.find { it.bookId == issuanceModel.bookId }

        // Если есть, то удалить бронь и создать выдачу
        if (reservation != null) {
            reservationRepository.deleteById(reservation.id)
            return issuanceRepository.create(issuanceModel)
        }

        // Иначе выдать только при количестве экземпляров > 0
        if (book.availableCopies <= 0) {
            throw BookNoAvailableCopiesException(book.id)
        }

        return issuanceRepository.create(issuanceModel)
    }
}