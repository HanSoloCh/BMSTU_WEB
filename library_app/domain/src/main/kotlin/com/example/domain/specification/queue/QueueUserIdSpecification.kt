package com.example.domain.specification.queue

import com.example.domain.model.QueueModel
import com.example.domain.specification.Specification
import java.util.UUID

class QueueUserIdSpecification(val userId: UUID) : Specification<QueueModel> {
    override fun specified(candidate: QueueModel): Boolean =
        candidate.userId == userId
}