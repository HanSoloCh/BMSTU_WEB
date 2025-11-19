package com.example.domain.usecase.user

import com.example.domain.model.UserModel
import com.example.domain.repository.UserRepository
import com.example.domain.specification.user.UserPhoneSpecification

class ReadUserByPhoneUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        phoneNumber: String,
        page: Int = 0,
        pageSize: Int = 20
    ): List<UserModel> {
        return userRepository.query(UserPhoneSpecification(phoneNumber), page, pageSize)
    }
}

