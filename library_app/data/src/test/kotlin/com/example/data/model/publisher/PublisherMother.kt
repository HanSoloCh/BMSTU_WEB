package com.example.data.model.publisher

import java.util.UUID

object PublisherMother {
    fun random() = PublisherBuilder()
        .withName("Publisher-${UUID.randomUUID().toString().take(5)}")
        .withDescription("Random publisher description")
        .withEmail("publisher@test.com")
        .withPhoneNumber("+123456789")
        .build()

    fun withId(id: UUID) = random().copy(id = id)
}