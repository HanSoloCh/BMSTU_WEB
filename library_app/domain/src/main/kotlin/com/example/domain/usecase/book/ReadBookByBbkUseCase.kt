package com.example.domain.usecase.book

import com.example.domain.model.BookModel
import com.example.domain.repository.BookRepository
import com.example.domain.specification.book.BookBbkIdSpecification
import java.util.UUID

class ReadBookByBbkUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bbkId: UUID): List<BookModel> {
        return bookRepository.query(BookBbkIdSpecification(bbkId))
    }
}