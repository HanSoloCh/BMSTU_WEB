package com.example.data.local.specification

import com.example.data.local.entity.BookEntity
import com.example.domain.model.BookModel
import com.example.domain.specification.AndSpecification
import com.example.domain.specification.Specification
import com.example.domain.specification.book.BookBbkIdSpecification
import com.example.domain.specification.book.BookIdSpecification
import com.example.domain.specification.book.BookPublisherIdSpecification
import com.example.domain.specification.book.BookTitleSpecification
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.lowerCase

object BookSpecToExpressionMapper {
    fun map(spec: Specification<BookModel>): Op<Boolean> = when (spec) {
        is AndSpecification<BookModel> -> spec.specifications.map { map(it) }
            .reduce { a, b -> a and b }

        is BookIdSpecification -> BookEntity.id eq spec.id
        is BookBbkIdSpecification -> BookEntity.bbkId eq spec.bbkId
        is BookPublisherIdSpecification -> BookEntity.publisherId eq spec.publisherId
        is BookTitleSpecification -> BookEntity.title.lowerCase() like "%${spec.title.lowercase()}%"

        else -> throw IllegalArgumentException("Unknown spec")
    }
}