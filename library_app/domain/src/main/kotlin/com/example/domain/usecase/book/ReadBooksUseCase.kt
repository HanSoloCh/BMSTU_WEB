package com.example.domain.usecase.book

import com.example.domain.model.BookModel
import com.example.domain.repository.BookRepository

class ReadBooksUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(page: Int, pageSize: Int): List<BookModel> {
        return bookRepository.readBooks(page, pageSize)
    }
}