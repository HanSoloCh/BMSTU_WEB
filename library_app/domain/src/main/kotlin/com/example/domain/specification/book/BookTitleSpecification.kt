package com.example.domain.specification.book

import com.example.domain.model.BookModel
import com.example.domain.specification.Specification

class BookTitleSpecification(val title: String) : Specification<BookModel> {
    override fun specified(candidate: BookModel): Boolean =
        candidate.title.equals(title, ignoreCase = true)
}