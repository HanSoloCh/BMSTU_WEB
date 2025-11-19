package com.example.domain.specification.user

import com.example.domain.model.UserModel
import com.example.domain.specification.Specification
import java.util.UUID

class UserIdSpecification(val id: UUID) : Specification<UserModel> {
    override fun specified(candidate: UserModel): Boolean = candidate.id == id
}