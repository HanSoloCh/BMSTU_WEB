package com.example.domain.unit.usecase.publisher

import com.example.domain.exception.ModelNotFoundException
import com.example.domain.model.publisher.PublisherMother
import com.example.domain.repository.PublisherRepository
import com.example.domain.usecase.publisher.UpdatePublisherUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class UpdatePublisherUseCaseTest {

    private val publisherRepository: PublisherRepository = mockk()
    private val updateUseCase = UpdatePublisherUseCase(publisherRepository)

    private val testPublisher = PublisherMother.random()

    @Test
    fun `update existing publisher`() = runTest {
        coEvery { publisherRepository.isContain(any()) } returns true
        coEvery { publisherRepository.update(testPublisher) } returns 1

        updateUseCase(testPublisher)

        coVerify { publisherRepository.isContain(any()) }
        coVerify { publisherRepository.update(testPublisher) }
    }

    @Test
    fun `update non-existing publisher throws exception`() = runTest {
        coEvery { publisherRepository.isContain(any()) } returns false

        assertFailsWith<ModelNotFoundException> {
            updateUseCase(testPublisher)
        }

        coVerify { publisherRepository.isContain(any()) }
        coVerify(exactly = 0) { publisherRepository.update(any()) }
    }
}
