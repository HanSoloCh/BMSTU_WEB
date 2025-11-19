package com.example.domain.specification

interface Specification<T> {
    fun specified(candidate: T): Boolean
}