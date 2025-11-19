package com.example.domain.specification.bbk

import com.example.domain.model.BbkModel
import com.example.domain.specification.Specification
import java.util.UUID

class BbkIdSpecification(val id: UUID) : Specification<BbkModel> {
    override fun specified(candidate: BbkModel): Boolean = candidate.id == id
}