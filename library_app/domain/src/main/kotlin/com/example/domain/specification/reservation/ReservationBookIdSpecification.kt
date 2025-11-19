package com.example.domain.specification.reservation

import com.example.domain.model.ReservationModel
import com.example.domain.specification.Specification
import java.util.UUID

class ReservationBookIdSpecification(val bookId: UUID) : Specification<ReservationModel> {
    override fun specified(candidate: ReservationModel): Boolean =
        candidate.bookId == bookId
}