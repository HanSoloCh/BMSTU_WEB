package com.example.domain.usecase.book

import com.example.domain.repository.BookRepository
import java.util.UUID

class DeleteBookUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookId: UUID) {
        bookRepository.deleteById(bookId)
    }
}