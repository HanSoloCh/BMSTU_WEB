package com.example.domain.exception

class EmptyStringException(string: String) :
    BaseDomainException("$string must be not empty.")