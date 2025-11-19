package com.example.domain.specification

class AndSpecification<T>(
    val specifications: List<Specification<T>>
) : Specification<T> {
    override fun specified(candidate: T): Boolean = specifications.all { it.specified(candidate) }
}