package com.example.domain.usecase.author

import com.example.domain.model.AuthorModel
import com.example.domain.repository.AuthorRepository
import com.example.domain.specification.author.AuthorNameSpecification

class ReadAuthorByNameUseCase(
    private val authorRepository: AuthorRepository
) {
    suspend operator fun invoke(
        authorName: String,
        page: Int = 0,
        pageSize: Int = 20
    ): List<AuthorModel> {
        return authorRepository.query(AuthorNameSpecification(authorName), page, pageSize)
    }
}