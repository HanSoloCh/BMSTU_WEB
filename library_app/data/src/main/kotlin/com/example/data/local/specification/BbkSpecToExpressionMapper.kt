package com.example.data.local.specification

import com.example.data.local.entity.BbkEntity
import com.example.domain.model.BbkModel
import com.example.domain.specification.AndSpecification
import com.example.domain.specification.Specification
import com.example.domain.specification.bbk.BbkCodeSpecification
import com.example.domain.specification.bbk.BbkIdSpecification
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.lowerCase

object BbkSpecToExpressionMapper {
    fun map(spec: Specification<BbkModel>): Op<Boolean> = when (spec) {
        is AndSpecification<BbkModel> -> spec.specifications.map { map(it) }
            .reduce { a, b -> a and b }

        is BbkCodeSpecification -> BbkEntity.code.lowerCase() like "%${spec.code.lowercase()}%"
        is BbkIdSpecification -> BbkEntity.id eq spec.id
        else -> throw IllegalArgumentException("Unknown spec")
    }
}