package com.example.domain.usecase.user

import com.example.domain.model.UserModel
import com.example.domain.repository.UserRepository
import java.util.UUID

class ReadUserByIdUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: UUID): UserModel? {
        return userRepository.readById(userId)
    }
}

