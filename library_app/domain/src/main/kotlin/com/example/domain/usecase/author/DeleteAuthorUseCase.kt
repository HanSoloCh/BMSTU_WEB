package com.example.domain.usecase.author

import com.example.domain.repository.AuthorRepository
import java.util.UUID

class DeleteAuthorUseCase(
    private val authorRepository: AuthorRepository
) {
    suspend operator fun invoke(authorId: UUID) {
        authorRepository.deleteById(authorId)
    }
}