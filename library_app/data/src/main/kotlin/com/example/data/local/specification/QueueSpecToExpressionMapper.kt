package com.example.data.local.specification

import com.example.data.local.entity.QueueEntity
import com.example.domain.model.QueueModel
import com.example.domain.specification.AndSpecification
import com.example.domain.specification.Specification
import com.example.domain.specification.queue.QueueBookIdSpecification
import com.example.domain.specification.queue.QueueIdSpecification
import com.example.domain.specification.queue.QueueUserIdSpecification
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

object QueueSpecToExpressionMapper {
    fun map(spec: Specification<QueueModel>): Op<Boolean> = when (spec) {
        is AndSpecification<QueueModel> -> spec.specifications.map { map(it) }
            .reduce { a, b -> a and b }

        is QueueIdSpecification -> QueueEntity.id eq spec.id
        is QueueUserIdSpecification -> QueueEntity.userId eq spec.userId
        is QueueBookIdSpecification -> QueueEntity.bookId eq spec.bookId

        else -> throw IllegalArgumentException("Unknown spec")
    }
}