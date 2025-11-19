package com.example.domain.usecase.book

import com.example.domain.exception.ModelDuplicateException
import com.example.domain.exception.ModelNotFoundException
import com.example.domain.model.BookModel
import com.example.domain.repository.AuthorRepository
import com.example.domain.repository.BbkRepository
import com.example.domain.repository.BookRepository
import com.example.domain.repository.PublisherRepository
import com.example.domain.specification.author.AuthorIdSpecification
import com.example.domain.specification.bbk.BbkIdSpecification
import com.example.domain.specification.book.BookIdSpecification
import com.example.domain.specification.publisher.PublisherIdSpecification
import java.util.UUID

class CreateBookUseCase(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository,
    private val bbkRepository: BbkRepository,
    private val publisherRepository: PublisherRepository
) {
    suspend operator fun invoke(bookModel: BookModel): UUID {
        if (bookRepository.isContain(BookIdSpecification(bookModel.id)))
            throw ModelDuplicateException("Book", bookModel.id)

        if (!bbkRepository.isContain(BbkIdSpecification(bookModel.bbkId)))
            throw ModelNotFoundException("Bbk", bookModel.bbkId)

        if (bookModel.publisherId != null && !publisherRepository.isContain(
                PublisherIdSpecification(
                    bookModel.publisherId
                )
            )
        )
            throw ModelNotFoundException("Publisher", bookModel.publisherId)

        for (author in bookModel.authors) {
            if (!authorRepository.isContain(AuthorIdSpecification(author)))
                throw ModelNotFoundException("Author", author)
        }

        return bookRepository.create(bookModel)
    }
}