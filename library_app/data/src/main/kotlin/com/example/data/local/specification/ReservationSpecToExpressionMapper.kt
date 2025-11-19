package com.example.data.local.specification

import com.example.data.local.entity.ReservationEntity
import com.example.domain.model.ReservationModel
import com.example.domain.specification.AndSpecification
import com.example.domain.specification.Specification
import com.example.domain.specification.reservation.ReservationBookIdSpecification
import com.example.domain.specification.reservation.ReservationIdSpecification
import com.example.domain.specification.reservation.ReservationUserIdSpecification
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

object ReservationSpecToExpressionMapper {
    fun map(spec: Specification<ReservationModel>): Op<Boolean> = when (spec) {
        is AndSpecification<ReservationModel> -> spec.specifications.map { map(it) }
            .reduce { a, b -> a and b }

        is ReservationIdSpecification -> ReservationEntity.id eq spec.id
        is ReservationUserIdSpecification -> ReservationEntity.userId eq spec.userId
        is ReservationBookIdSpecification -> ReservationEntity.bookId eq spec.bookId

        else -> throw IllegalArgumentException("Unknown spec")
    }
}