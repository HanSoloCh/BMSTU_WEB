package com.example.domain.usecase.author

import com.example.domain.exception.ModelNotFoundException
import com.example.domain.model.AuthorModel
import com.example.domain.repository.AuthorRepository
import com.example.domain.specification.author.AuthorIdSpecification

class UpdateAuthorUseCase(
    private val authorRepository: AuthorRepository
) {
    suspend operator fun invoke(authorModel: AuthorModel) {
        if (!authorRepository.isContain(AuthorIdSpecification(authorModel.id)))
            throw ModelNotFoundException("Author", authorModel.id)

        authorRepository.update(authorModel)
    }
}