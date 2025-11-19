package com.example.domain.specification.issuance

import com.example.domain.model.IssuanceModel
import com.example.domain.specification.Specification
import java.util.UUID

class IssuanceIdSpecification(val id: UUID) : Specification<IssuanceModel> {
    override fun specified(candidate: IssuanceModel): Boolean =
        candidate.id == id
}