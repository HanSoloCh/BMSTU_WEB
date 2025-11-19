package com.example.domain.specification.publisher

import com.example.domain.model.PublisherModel
import com.example.domain.specification.Specification

class PublisherNameSpecification(val name: String) : Specification<PublisherModel> {
    override fun specified(candidate: PublisherModel): Boolean =
        candidate.name.equals(name, ignoreCase = true)
}