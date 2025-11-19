package com.example.domain.exception

class InvalidDateException(string: String) : BaseDomainException("Invalid date: $string")