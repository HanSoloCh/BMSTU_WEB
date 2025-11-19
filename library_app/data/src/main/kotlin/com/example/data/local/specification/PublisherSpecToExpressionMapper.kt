package com.example.data.local.specification

import com.example.data.local.entity.PublisherEntity
import com.example.domain.model.PublisherModel
import com.example.domain.specification.AndSpecification
import com.example.domain.specification.Specification
import com.example.domain.specification.publisher.PublisherIdSpecification
import com.example.domain.specification.publisher.PublisherNameSpecification
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.lowerCase

object PublisherSpecToExpressionMapper {
    fun map(spec: Specification<PublisherModel>): Op<Boolean> = when (spec) {
        is AndSpecification<PublisherModel> -> spec.specifications.map { map(it) }
            .reduce { a, b -> a and b }

        is PublisherIdSpecification -> PublisherEntity.id eq spec.id
        is PublisherNameSpecification -> PublisherEntity.name.lowerCase() like "%${spec.name.lowercase()}%"
        else -> throw IllegalArgumentException("Unknown spec")
    }
}