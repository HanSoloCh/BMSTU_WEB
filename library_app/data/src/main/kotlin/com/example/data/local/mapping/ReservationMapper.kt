package com.example.data.local.mapping

import com.example.data.local.entity.ReservationEntity
import com.example.domain.model.ReservationModel
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

object ReservationMapper {
    fun toDomain(row: ResultRow): ReservationModel {
        return ReservationModel(
            id = row[ReservationEntity.id].value,
            bookId = row[ReservationEntity.bookId].value,
            userId = row[ReservationEntity.userId].value,
            reservationDate = row[ReservationEntity.reservationDate].toJavaLocalDate(),
            cancelDate = row[ReservationEntity.cancelDate].toJavaLocalDate(),
        )
    }

    fun toInsertStatement(
        reservationModel: ReservationModel,
        statement: InsertStatement<EntityID<UUID>>
    ): InsertStatement<EntityID<UUID>> {
        return statement.also {
            it[ReservationEntity.id] = reservationModel.id
            it[ReservationEntity.bookId] = reservationModel.bookId
            it[ReservationEntity.userId] = reservationModel.userId
            it[ReservationEntity.reservationDate] =
                reservationModel.reservationDate.toKotlinLocalDate()
            it[ReservationEntity.cancelDate] = reservationModel.cancelDate.toKotlinLocalDate()
        }
    }

    fun toUpdateStatement(
        reservationModel: ReservationModel,
        statement: UpdateStatement
    ): UpdateStatement {
        return statement.also {
            it[ReservationEntity.id] = reservationModel.id
            it[ReservationEntity.bookId] = reservationModel.bookId
            it[ReservationEntity.userId] = reservationModel.userId
            it[ReservationEntity.reservationDate] =
                reservationModel.reservationDate.toKotlinLocalDate()
            it[ReservationEntity.cancelDate] = reservationModel.cancelDate.toKotlinLocalDate()
        }
    }

}