package com.example.domain.model

import com.example.domain.common.Regexes
import com.example.domain.enums.UserRole
import com.example.domain.exception.EmptyStringException
import com.example.domain.exception.InvalidEmailException
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserModel(
    val id: @Contextual UUID = UUID.randomUUID(),
    val name: String,
    val surname: String,
    val secondName: String? = null,
    val password: String,
    val phoneNumber: String,
    val email: String? = null,
    val role: UserRole,
) {
    init {
        when {
            name.isBlank() -> throw EmptyStringException("name")
            surname.isBlank() -> throw EmptyStringException("surname")
            secondName != null && secondName.isBlank() -> throw EmptyStringException("secondName")
            password.isBlank() -> throw EmptyStringException("password")
            email != null && !isValidEmail(email) -> throw InvalidEmailException(email)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regexes.EMAIL_ADDRESS)
    }

    private fun isValidPhone(phone: String): Boolean {
        return phone.matches(Regexes.PHONE_NUMBER)
    }
}
