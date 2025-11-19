package com.example.domain.specification.book

import com.example.domain.model.BookModel
import com.example.domain.specification.Specification
import java.util.UUID

class BookPublisherIdSpecification(val publisherId: UUID) : Specification<BookModel> {
    override fun specified(candidate: BookModel): Boolean =
        candidate.publisherId == publisherId
}