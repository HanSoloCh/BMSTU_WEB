package com.example.data.local.repository

import com.example.data.local.entity.ReservationEntity
import com.example.data.local.mapping.ReservationMapper
import com.example.data.local.specification.ReservationSpecToExpressionMapper
import com.example.domain.model.ReservationModel
import com.example.domain.repository.ReservationRepository
import com.example.domain.specification.Specification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

class ReservationRepositoryImpl(
    private val db: Database
) : ReservationRepository {
    override suspend fun create(reservationModel: ReservationModel) = withContext(Dispatchers.IO) {
        transaction(db) {
            ReservationEntity.insertAndGetId {
                ReservationMapper.toInsertStatement(reservationModel, it)
            }.value
        }
    }

    override suspend fun update(reservationModel: ReservationModel) = withContext(Dispatchers.IO) {
        transaction(db) {
            ReservationEntity.update({ ReservationEntity.id eq reservationModel.id }) {
                ReservationMapper.toUpdateStatement(reservationModel, it)
            }
        }
    }

    override suspend fun deleteById(reservationId: UUID) = withContext(Dispatchers.IO) {
        transaction(db) {
            ReservationEntity.deleteWhere { id eq reservationId }
        }
    }

    override suspend fun isContain(spec: Specification<ReservationModel>) =
        withContext(Dispatchers.IO) {
            query(spec).isNotEmpty()
        }

    override suspend fun query(
        spec: Specification<ReservationModel>,
        page: Int,
        pageSize: Int
    ): List<ReservationModel> =
        withContext(Dispatchers.IO) {
            val expression = ReservationSpecToExpressionMapper.map(spec)
            val offset: Long = (page * pageSize).toLong()
            transaction(db) {
                ReservationEntity.selectAll().where { expression }.limit(pageSize, offset)
                    .map { ReservationMapper.toDomain(it) }
            }
        }
}