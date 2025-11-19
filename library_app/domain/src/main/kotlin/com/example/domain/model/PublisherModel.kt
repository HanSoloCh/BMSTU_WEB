package com.example.domain.model

import com.example.domain.common.Regexes
import com.example.domain.exception.EmptyStringException
import com.example.domain.exception.InvalidEmailException
import com.example.domain.exception.InvalidPhoneException
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PublisherModel(
    val id: @Contextual UUID = UUID.randomUUID(),
    val name: String,
    val description: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
) {
    init {
        when {
            name.isBlank() -> throw EmptyStringException("name")
            description != null && description.isBlank() -> throw EmptyStringException("description")

            email != null && !isValidEmail(email) -> throw InvalidEmailException(email)
            phoneNumber != null && !isValidPhone(phoneNumber) -> throw InvalidPhoneException(
                phoneNumber
            )
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regexes.EMAIL_ADDRESS)
    }

    private fun isValidPhone(phone: String): Boolean {
        return phone.matches(Regexes.PHONE_NUMBER)
    }
}