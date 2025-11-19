package com.example.domain.repository

import com.example.domain.model.BookModel
import com.example.domain.specification.Specification
import java.util.UUID

interface BookRepository {
    suspend fun readBooks(page: Int, pageSize: Int): List<BookModel>
    suspend fun readById(bookId: UUID): BookModel?

    suspend fun readByAuthorId(authorId: UUID): List<BookModel>

    suspend fun create(bookModel: BookModel): UUID

    suspend fun update(bookModel: BookModel): Int

    suspend fun deleteById(bookId: UUID): Int

    suspend fun isContain(spec: Specification<BookModel>): Boolean

    suspend fun query(spec: Specification<BookModel>): List<BookModel>
}