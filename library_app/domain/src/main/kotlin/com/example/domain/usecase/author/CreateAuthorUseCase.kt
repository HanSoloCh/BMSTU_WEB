package com.example.domain.usecase.author

import com.example.domain.exception.ModelDuplicateException
import com.example.domain.model.AuthorModel
import com.example.domain.repository.AuthorRepository
import com.example.domain.specification.author.AuthorIdSpecification
import java.util.UUID

class CreateAuthorUseCase(
    private val authorRepository: AuthorRepository
) {
    suspend operator fun invoke(authorModel: AuthorModel): UUID {
        if (authorRepository.isContain(AuthorIdSpecification(authorModel.id)))
            throw ModelDuplicateException("Author", authorModel.id)

        return authorRepository.create(authorModel)
    }
}