package com.example.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class ReservationModel(
    val id: @Contextual UUID = UUID.randomUUID(),
    val bookId: @Contextual UUID,
    val userId: @Contextual UUID,
    val reservationDate: @Contextual LocalDate,
    val cancelDate: @Contextual LocalDate,
) {
    init {
        require(reservationDate <= LocalDate.now())
        require(cancelDate > reservationDate)
    }
}
