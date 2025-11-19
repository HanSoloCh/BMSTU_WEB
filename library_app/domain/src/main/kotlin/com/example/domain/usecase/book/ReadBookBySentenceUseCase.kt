package com.example.domain.usecase.book

import com.example.domain.model.BookModel
import com.example.domain.repository.ApuRepository
import com.example.domain.repository.BookRepository
import com.example.domain.specification.apu.ApuTermSpecification
import com.example.domain.specification.book.BookBbkIdSpecification
import com.example.domain.specification.book.BookTitleSpecification

class ReadBookBySentenceUseCase(
    private val apuRepository: ApuRepository,
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(sentence: String): List<BookModel> {
        val apuList = apuRepository.query(ApuTermSpecification(sentence))
        val spec = if (apuList.isNotEmpty()) {
            BookBbkIdSpecification(apuList.first().bbkId)
        } else {
            BookTitleSpecification(sentence)
        }
        return bookRepository.query(spec)
    }
}