package com.example.data.local.specification

import com.example.data.local.entity.AuthorEntity
import com.example.domain.model.AuthorModel
import com.example.domain.specification.AndSpecification
import com.example.domain.specification.Specification
import com.example.domain.specification.author.AuthorIdSpecification
import com.example.domain.specification.author.AuthorNameSpecification
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.lowerCase


object AuthorSpecToExpressionMapper {
    fun map(spec: Specification<AuthorModel>): Op<Boolean> = when (spec) {
        is AndSpecification<AuthorModel> -> spec.specifications.map { map(it) }
            .reduce { a, b -> a and b }

        is AuthorIdSpecification -> AuthorEntity.id eq spec.id
        is AuthorNameSpecification -> AuthorEntity.name.lowerCase() like "%${spec.name.lowercase()}%"

        else -> throw IllegalArgumentException("Unknown spec")
    }
}