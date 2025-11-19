package com.example.domain.specification.author

import com.example.domain.model.AuthorModel
import com.example.domain.specification.Specification

class AuthorNameSpecification(val name: String) : Specification<AuthorModel> {
    override fun specified(candidate: AuthorModel): Boolean =
        candidate.name.equals(name, ignoreCase = true)
}