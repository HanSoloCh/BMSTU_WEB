package com.example.domain.usecase.user

import com.example.domain.repository.UserRepository
import java.util.UUID

class DeleteUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: UUID) {
        userRepository.deleteById(userId)
    }
}