package com.example.data.local.specification

import com.example.data.local.entity.ApuEntity
import com.example.domain.model.ApuModel
import com.example.domain.specification.AndSpecification
import com.example.domain.specification.Specification
import com.example.domain.specification.apu.ApuIdSpecification
import com.example.domain.specification.apu.ApuTermSpecification
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.lowerCase

object ApuSpecToExpressionMapper {
    fun map(spec: Specification<ApuModel>): Op<Boolean> = when (spec) {
        is AndSpecification<ApuModel> -> spec.specifications.map { map(it) }
            .reduce { a, b -> a and b }

        is ApuIdSpecification -> ApuEntity.id eq spec.id
        is ApuTermSpecification -> ApuEntity.term.lowerCase() like "%${spec.term.lowercase()}%"

        else -> throw IllegalArgumentException("Unknown spec")
    }
}