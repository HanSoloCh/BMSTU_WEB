package com.example.data.local.entity

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.date

object ReservationEntity : UUIDTable("reservation") {
    val bookId = reference("book_id", BookEntity, onDelete = ReferenceOption.CASCADE)
    val userId = reference("user_id", UserEntity, onDelete = ReferenceOption.CASCADE)
    val reservationDate = date("reservation_date")
    val cancelDate = date("cancel_date")
}
