package com.example.domain.usecase.queue

import com.example.domain.exception.ModelNotFoundException
import com.example.domain.repository.BookRepository
import com.example.domain.repository.QueueRepository
import com.example.domain.repository.UserRepository
import com.example.domain.specification.book.BookIdSpecification
import com.example.domain.specification.user.UserIdSpecification
import java.util.UUID

class GetQueueUseCase(
    private val queueRepository: QueueRepository,
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookId: UUID, userId: UUID): Int? {
        if (!userRepository.isContain(UserIdSpecification(userId)))
            throw ModelNotFoundException("User", userId)

        if (!bookRepository.isContain(BookIdSpecification(bookId)))
            throw ModelNotFoundException("Book", bookId)

        return queueRepository.getQueuePosition(bookId, userId)
    }
}