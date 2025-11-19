package com.example.domain.usecase.book

import com.example.domain.model.BookModel
import com.example.domain.repository.BookRepository
import java.util.UUID

class ReadBookByAuthorUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(authorId: UUID): List<BookModel> {
        return bookRepository.readByAuthorId(authorId)
    }
}