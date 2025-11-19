package com.example.domain.specification.author

import com.example.domain.model.AuthorModel
import com.example.domain.specification.Specification
import java.util.UUID

class AuthorIdSpecification(val id: UUID) : Specification<AuthorModel> {
    override fun specified(candidate: AuthorModel): Boolean = candidate.id == id
}