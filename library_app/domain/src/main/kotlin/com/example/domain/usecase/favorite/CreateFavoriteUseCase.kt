package com.example.domain.usecase.favorite

import com.example.domain.exception.ModelNotFoundException
import com.example.domain.repository.BookRepository
import com.example.domain.repository.UserFavoriteRepository
import com.example.domain.repository.UserRepository
import com.example.domain.specification.book.BookIdSpecification
import com.example.domain.specification.user.UserIdSpecification
import java.util.UUID

class CreateFavoriteUseCase(
    private val userFavoriteRepository: UserFavoriteRepository,
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(userId: UUID, bookId: UUID): Pair<UUID, UUID> {
        if (!userRepository.isContain(UserIdSpecification(userId)))
            throw ModelNotFoundException("User", userId)

        if (!bookRepository.isContain(BookIdSpecification(bookId)))
            throw ModelNotFoundException("Book", bookId)

        return userFavoriteRepository.create(userId, bookId)
    }
}