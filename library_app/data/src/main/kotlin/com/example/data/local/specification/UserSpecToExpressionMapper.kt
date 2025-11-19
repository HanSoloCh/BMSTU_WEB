package com.example.data.local.specification

import com.example.data.local.entity.UserEntity
import com.example.domain.model.UserModel
import com.example.domain.specification.AndSpecification
import com.example.domain.specification.Specification
import com.example.domain.specification.user.UserIdSpecification
import com.example.domain.specification.user.UserPhoneSpecification
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and


object UserSpecToExpressionMapper {
    fun map(spec: Specification<UserModel>): Op<Boolean> = when (spec) {
        is AndSpecification<UserModel> -> spec.specifications.map { map(it) }
            .reduce { a, b -> a and b }

        is UserIdSpecification -> UserEntity.id eq spec.id
        is UserPhoneSpecification -> UserEntity.phoneNumber like "%${spec.phoneNumber}%"

        else -> throw IllegalArgumentException("Unknown spec")
    }
}