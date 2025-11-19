package com.example.domain.repository

import com.example.domain.model.ReservationModel
import com.example.domain.specification.Specification
import java.util.UUID

interface ReservationRepository {
    suspend fun create(reservationModel: ReservationModel): UUID

    suspend fun update(reservationModel: ReservationModel): Int

    suspend fun deleteById(reservationId: UUID): Int

    suspend fun isContain(spec: Specification<ReservationModel>): Boolean

    suspend fun query(
        spec: Specification<ReservationModel>,
        page: Int = 0,
        pageSize: Int = 20
    ): List<ReservationModel>
}