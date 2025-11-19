package com.example.domain.specification.user

import com.example.domain.model.UserModel
import com.example.domain.specification.Specification

class UserPhoneSpecification(val phoneNumber: String) : Specification<UserModel> {
    override fun specified(candidate: UserModel): Boolean =
        candidate.phoneNumber.equals(phoneNumber, ignoreCase = true)
}