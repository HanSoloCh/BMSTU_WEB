package com.example.domain.usecase.book

import com.example.domain.model.BookModel
import com.example.domain.repository.BookRepository
import com.example.domain.specification.book.BookPublisherIdSpecification
import java.util.UUID

class ReadBookByPublisherUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(publisherId: UUID): List<BookModel> {
        return bookRepository.query(BookPublisherIdSpecification(publisherId))
    }
}