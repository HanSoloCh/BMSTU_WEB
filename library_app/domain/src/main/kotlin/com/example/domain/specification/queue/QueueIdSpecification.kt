package com.example.domain.specification.queue

import com.example.domain.model.QueueModel
import com.example.domain.specification.Specification
import java.util.UUID

class QueueIdSpecification(val id: UUID) : Specification<QueueModel> {
    override fun specified(candidate: QueueModel): Boolean =
        candidate.id == id
}