package com.example.domain.exception

class InvalidPhoneException(phone: String) :
    BaseDomainException("Invalid phone number $phone")