package com.example.domain.specification.publisher

import com.example.domain.model.PublisherModel
import com.example.domain.specification.Specification
import java.util.UUID

class PublisherIdSpecification(val id: UUID) : Specification<PublisherModel> {
    override fun specified(candidate: PublisherModel): Boolean =
        candidate.id == id
}