package com.example.domain.usecase

import com.example.domain.model.UserModel
import com.example.domain.repository.UserRepository

class LoginUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(phone: String, password: String): UserModel? {
        if (phone.isBlank() || password.isBlank()) {
            return null
        }
        return userRepository.login(phone, hashPassword(password))
    }

    private fun hashPassword(password: String): String {
        return password
    }
}