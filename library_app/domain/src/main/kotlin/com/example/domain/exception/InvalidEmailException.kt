package com.example.domain.exception

class InvalidEmailException(email: String) :
    BaseDomainException("Invalid email: $email")