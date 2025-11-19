package com.example.data.model.publisher

import com.example.domain.model.PublisherModel
import java.util.UUID

class PublisherBuilder {
    private var id: UUID = UUID.randomUUID()
    private var name: String = ""
    private var description: String? = null
    private var email: String? = null
    private var phoneNumber: String? = null

    fun withId(id: UUID) = apply { this.id = id }
    fun withName(name: String) = apply { this.name = name }
    fun withDescription(description: String?) = apply { this.description = description }
    fun withEmail(email: String?) = apply { this.email = email }
    fun withPhoneNumber(phone: String?) = apply { this.phoneNumber = phone }

    fun build() = PublisherModel(
        id = id,
        name = name,
        description = description,
        email = email,
        phoneNumber = phoneNumber
    )
}