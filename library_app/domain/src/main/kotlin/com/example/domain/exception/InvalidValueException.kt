package com.example.domain.exception

class InvalidValueException(name: String, value: String) :
    BaseDomainException("Invalid Value for $name: $value")