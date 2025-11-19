package com.example.domain.model.publisher

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
    fun withDescription(description: String?) = also { this.description = description }
    fun withEmail(email: String?) = also { this.email = email }
    fun withPhoneNumber(phoneNumber: String?) = also { this.phoneNumber = phoneNumber }

    fun build() = PublisherModel(
        id = id,
        name = name,
        description = description,
        email = email,
        phoneNumber = phoneNumber
    )
}