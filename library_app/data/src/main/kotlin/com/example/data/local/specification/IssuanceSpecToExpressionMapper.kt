package com.example.data.local.specification

import com.example.data.local.entity.IssuanceEntity
import com.example.domain.model.IssuanceModel
import com.example.domain.specification.AndSpecification
import com.example.domain.specification.Specification
import com.example.domain.specification.issuance.IssuanceBookIdSpecification
import com.example.domain.specification.issuance.IssuanceIdSpecification
import com.example.domain.specification.issuance.IssuanceUserIdSpecification
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

object IssuanceSpecToExpressionMapper {
    fun map(spec: Specification<IssuanceModel>): Op<Boolean> = when (spec) {
        is AndSpecification<IssuanceModel> -> spec.specifications.map { map(it) }
            .reduce { a, b -> a and b }

        is IssuanceIdSpecification -> IssuanceEntity.id eq spec.id
        is IssuanceUserIdSpecification -> IssuanceEntity.userId eq spec.userId
        is IssuanceBookIdSpecification -> IssuanceEntity.bookId eq spec.bookId
        else -> throw IllegalArgumentException("Unknown spec")
    }
}