package com.example.domain.unit.usecase.author

import com.example.domain.exception.ModelDuplicateException
import com.example.domain.model.AuthorModel
import com.example.domain.model.TestAuthor
import com.example.domain.repository.AuthorRepository
import com.example.domain.usecase.author.CreateAuthorUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class CreateAuthorUseCaseTest {
    private val authorRepository: AuthorRepository = mockk()
    private val createAuthorUseCase = CreateAuthorUseCase(authorRepository)

    private lateinit var testAuthor: AuthorModel

    @Before
    fun setUp() {
        testAuthor = TestAuthor.create()
    }

    @Test
    fun `simple create author test`() = runTest {
        coEvery { authorRepository.isContain(any()) } returns false
        coEvery { authorRepository.create(testAuthor) } returns testAuthor.id

        val createdId = createAuthorUseCase(testAuthor)

        assertEquals(testAuthor.id, createdId)

        coVerify { authorRepository.isContain(any()) }
        coVerify { authorRepository.create(testAuthor) }
    }

    @Test
    fun `crate duplicate author test`() = runTest {
        coEvery { authorRepository.isContain(any()) } returns true

        assertFailsWith<ModelDuplicateException> {
            createAuthorUseCase(testAuthor)
        }
        coVerify { authorRepository.isContain(any()) }
        coVerify(exactly = 0) { authorRepository.create(any()) }
    }

}