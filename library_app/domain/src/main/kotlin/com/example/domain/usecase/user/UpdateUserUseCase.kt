package com.example.domain.usecase.user

import com.example.domain.exception.ModelNotFoundException
import com.example.domain.model.UserModel
import com.example.domain.repository.UserRepository
import com.example.domain.specification.user.UserIdSpecification

class UpdateUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userModel: UserModel) {
        if (!userRepository.isContain(UserIdSpecification(userModel.id)))
            throw ModelNotFoundException("User", userModel.id)

        userRepository.update(userModel)
    }
}