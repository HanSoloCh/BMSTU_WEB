package com.example.domain.model

import java.util.UUID

internal object TestApu {
    fun create(
        id: UUID = UUID.randomUUID(),
        term: String = "Test term",
        bbkId: UUID
    ) = ApuModel(
        id = id,
        term = term,
        bbkId = bbkId
    )
}
