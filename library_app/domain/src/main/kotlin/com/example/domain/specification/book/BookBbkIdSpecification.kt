package com.example.domain.specification.book

import com.example.domain.model.BookModel
import com.example.domain.specification.Specification
import java.util.UUID

class BookBbkIdSpecification(val bbkId: UUID) : Specification<BookModel> {
    override fun specified(candidate: BookModel): Boolean =
        candidate.bbkId == bbkId
}