package com.example.domain.usecase.author

import com.example.domain.model.AuthorModel
import com.example.domain.repository.AuthorRepository
import java.util.UUID

class ReadAuthorByIdUseCase(
    private val authorRepository: AuthorRepository
) {
    suspend operator fun invoke(authorId: UUID): AuthorModel? {
        return authorRepository.readById(authorId)
    }
}