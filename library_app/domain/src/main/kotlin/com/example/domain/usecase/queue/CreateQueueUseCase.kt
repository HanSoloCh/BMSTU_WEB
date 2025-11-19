package com.example.domain.usecase.queue

import com.example.domain.exception.ModelDuplicateException
import com.example.domain.exception.ModelNotFoundException
import com.example.domain.model.QueueModel
import com.example.domain.repository.BookRepository
import com.example.domain.repository.QueueRepository
import com.example.domain.repository.UserRepository
import com.example.domain.specification.book.BookIdSpecification
import com.example.domain.specification.queue.QueueIdSpecification
import com.example.domain.specification.user.UserIdSpecification
import java.util.UUID

class CreateQueueUseCase(
    private val queueRepository: QueueRepository,
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(queueModel: QueueModel): UUID {
        if (queueRepository.isContain(QueueIdSpecification(queueModel.id)))
            throw ModelDuplicateException("Queue", queueModel.id)

        if (!userRepository.isContain(UserIdSpecification(queueModel.userId)))
            throw ModelNotFoundException("User", queueModel.userId)

        if (!bookRepository.isContain(BookIdSpecification(queueModel.bookId)))
            throw ModelNotFoundException("Book", queueModel.bookId)

        return queueRepository.create(queueModel)
    }
}