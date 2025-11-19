package com.example.domain.usecase.user

import com.example.domain.exception.ModelDuplicateException
import com.example.domain.model.UserModel
import com.example.domain.repository.UserRepository
import com.example.domain.specification.user.UserIdSpecification
import java.util.UUID

class CreateUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userModel: UserModel): UUID {
        if (userRepository.isContain(UserIdSpecification(userModel.id)))
            throw ModelDuplicateException("User", userModel.id)

        return userRepository.create(userModel)
    }
}