package com.example.domain.unit.usecase.publisher

import com.example.domain.model.publisher.PublisherMother
import com.example.domain.repository.PublisherRepository
import com.example.domain.usecase.publisher.DeletePublisherUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class DeletePublisherUseCaseTest {

    private val publisherRepository: PublisherRepository = mockk()
    private val deleteUseCase = DeletePublisherUseCase(publisherRepository)

    private val testPublisher = PublisherMother.random()

    @Test
    fun `delete existing publisher`() = runTest {
        // Arrange
        coEvery { publisherRepository.deleteById(testPublisher.id) } returns 1

        // Act
        deleteUseCase(testPublisher.id)

        // Assert
        coVerify { publisherRepository.deleteById(testPublisher.id) }
    }

    @Test
    fun `delete non-existing publisher does nothing`() = runTest {
        // Arrange
        coEvery { publisherRepository.deleteById(testPublisher.id) } returns 0

        // Act
        deleteUseCase(testPublisher.id)

        // Assert
        coVerify { publisherRepository.deleteById(testPublisher.id) }
    }
}
