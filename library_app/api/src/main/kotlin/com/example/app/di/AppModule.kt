package com.example.app.di

import com.example.data.local.DatabaseBuilder
import com.example.data.local.repository.ApuRepositoryImpl
import com.example.data.local.repository.AuthorRepositoryImpl
import com.example.data.local.repository.BbkRepositoryImpl
import com.example.data.local.repository.BookRepositoryImpl
import com.example.data.local.repository.IssuanceRepositoryImpl
import com.example.data.local.repository.PublisherRepositoryImpl
import com.example.data.local.repository.QueueRepositoryImpl
import com.example.data.local.repository.ReservationRepositoryImpl
import com.example.data.local.repository.UserFavoriteRepositoryImpl
import com.example.data.local.repository.UserRepositoryImpl
import com.example.domain.repository.ApuRepository
import com.example.domain.repository.AuthorRepository
import com.example.domain.repository.BbkRepository
import com.example.domain.repository.BookRepository
import com.example.domain.repository.IssuanceRepository
import com.example.domain.repository.PublisherRepository
import com.example.domain.repository.QueueRepository
import com.example.domain.repository.ReservationRepository
import com.example.domain.repository.UserFavoriteRepository
import com.example.domain.repository.UserRepository
import com.example.domain.usecase.LoginUserUseCase
import com.example.domain.usecase.apu.CreateApuUseCase
import com.example.domain.usecase.apu.DeleteApuUseCase
import com.example.domain.usecase.apu.ReadApuByIdUseCase
import com.example.domain.usecase.apu.ReadApuByTermUseCase
import com.example.domain.usecase.apu.UpdateApuUseCase
import com.example.domain.usecase.author.CreateAuthorUseCase
import com.example.domain.usecase.author.DeleteAuthorUseCase
import com.example.domain.usecase.author.ReadAuthorByIdUseCase
import com.example.domain.usecase.author.ReadAuthorByNameUseCase
import com.example.domain.usecase.author.UpdateAuthorUseCase
import com.example.domain.usecase.bbk.CreateBbkUseCase
import com.example.domain.usecase.bbk.DeleteBbkUseCase
import com.example.domain.usecase.bbk.ReadBbkByCodeUseCase
import com.example.domain.usecase.bbk.ReadBbkByIdUseCase
import com.example.domain.usecase.bbk.UpdateBbkUseCase
import com.example.domain.usecase.book.CreateBookUseCase
import com.example.domain.usecase.book.DeleteBookUseCase
import com.example.domain.usecase.book.ReadBookByAuthorUseCase
import com.example.domain.usecase.book.ReadBookByBbkUseCase
import com.example.domain.usecase.book.ReadBookByIdUseCase
import com.example.domain.usecase.book.ReadBookByPublisherUseCase
import com.example.domain.usecase.book.ReadBookBySentenceUseCase
import com.example.domain.usecase.book.ReadBooksUseCase
import com.example.domain.usecase.book.UpdateBookUseCase
import com.example.domain.usecase.favorite.CreateFavoriteUseCase
import com.example.domain.usecase.favorite.DeleteFavoriteUseCase
import com.example.domain.usecase.favorite.ReadFavoriteByUserIdUseCase
import com.example.domain.usecase.issuance.CreateIssuanceUseCase
import com.example.domain.usecase.issuance.DeleteIssuanceUseCase
import com.example.domain.usecase.issuance.ReadIssuanceUseCase
import com.example.domain.usecase.issuance.UpdateIssuanceUseCase
import com.example.domain.usecase.publisher.CreatePublisherUseCase
import com.example.domain.usecase.publisher.DeletePublisherUseCase
import com.example.domain.usecase.publisher.ReadPublisherByIdUseCase
import com.example.domain.usecase.publisher.ReadPublisherByNameUseCase
import com.example.domain.usecase.publisher.UpdatePublisherUseCase
import com.example.domain.usecase.queue.CreateQueueUseCase
import com.example.domain.usecase.queue.DeleteQueueUseCase
import com.example.domain.usecase.queue.GetQueueUseCase
import com.example.domain.usecase.queue.ReadQueueUseCase
import com.example.domain.usecase.queue.UpdateQueueUseCase
import com.example.domain.usecase.reservation.CreateReservationUseCase
import com.example.domain.usecase.reservation.DeleteReservationUseCase
import com.example.domain.usecase.reservation.ReadReservationUseCase
import com.example.domain.usecase.reservation.UpdateReservationUseCase
import com.example.domain.usecase.user.CreateUserUseCase
import com.example.domain.usecase.user.DeleteUserUseCase
import com.example.domain.usecase.user.ReadUserByIdUseCase
import com.example.domain.usecase.user.ReadUserByPhoneUseCase
import com.example.domain.usecase.user.UpdateUserUseCase
import io.ktor.server.config.ApplicationConfig
import org.koin.dsl.module
import javax.sql.DataSource

fun appModule(config: ApplicationConfig) = module {
    single {
        DatabaseBuilder.DatabaseConfig(
            url = config.property("ktor.database.url").getString(),
            driver = config.property("ktor.database.driver").getString(),
            username = config.property("ktor.database.user").getString(),
            password = config.property("ktor.database.password").getString(),
            maximumPoolSize = config.property("ktor.database.maxPoolSize").getString().toInt()
        )
    }
    single<DataSource> { DatabaseBuilder.createDataSource(get()) }
    single {
        val db = DatabaseBuilder.connect(get())
        DatabaseBuilder.runMigrations(db)
        db
    }

    single<ApuRepository> { ApuRepositoryImpl(get()) }
    single<AuthorRepository> { AuthorRepositoryImpl(get()) }
    single<BbkRepository> { BbkRepositoryImpl(get()) }
    single<PublisherRepository> { PublisherRepositoryImpl(get()) }
    single<BookRepository> { BookRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<QueueRepository> { QueueRepositoryImpl(get()) }
    single<ReservationRepository> { ReservationRepositoryImpl(get()) }
    single<IssuanceRepository> { IssuanceRepositoryImpl(get()) }
    single<UserFavoriteRepository> { UserFavoriteRepositoryImpl(get()) }

    // Author use case
    single { ReadAuthorByIdUseCase(get()) }
    single { CreateAuthorUseCase(get()) }
    single { UpdateAuthorUseCase(get()) }
    single { DeleteAuthorUseCase(get()) }
    single { ReadAuthorByNameUseCase(get()) }

    // Apu use case
    single { ReadApuByIdUseCase(get()) }
    single { CreateApuUseCase(get(), get()) }
    single { UpdateApuUseCase(get(), get()) }
    single { DeleteApuUseCase(get()) }
    single { ReadApuByTermUseCase(get()) }

    // Bbk use case
    single { ReadBbkByIdUseCase(get()) }
    single { CreateBbkUseCase(get()) }
    single { UpdateBbkUseCase(get()) }
    single { DeleteBbkUseCase(get()) }
    single { ReadBbkByCodeUseCase(get()) }

    // Publisher use case
    single { ReadPublisherByIdUseCase(get()) }
    single { CreatePublisherUseCase(get()) }
    single { UpdatePublisherUseCase(get()) }
    single { DeletePublisherUseCase(get()) }
    single { ReadPublisherByNameUseCase(get()) }

    // User use case
    single { ReadUserByIdUseCase(get()) }
    single { ReadUserByPhoneUseCase(get()) }
    single { CreateUserUseCase(get()) }
    single { UpdateUserUseCase(get()) }
    single { DeleteUserUseCase(get()) }
    single { LoginUserUseCase(get()) }

    // Reservation use case
    single { CreateReservationUseCase(get(), get(), get()) }
    single { UpdateReservationUseCase(get(), get(), get()) }
    single { DeleteReservationUseCase(get()) }
    single { ReadReservationUseCase(get()) }

    // Issuance use case
    single { CreateIssuanceUseCase(get(), get(), get(), get()) }
    single { UpdateIssuanceUseCase(get(), get(), get()) }
    single { DeleteIssuanceUseCase(get()) }
    single { ReadIssuanceUseCase(get()) }

    // Queue use case
    single { CreateQueueUseCase(get(), get(), get()) }
    single { UpdateQueueUseCase(get(), get(), get()) }
    single { DeleteQueueUseCase(get()) }
    single { ReadQueueUseCase(get()) }
    single { GetQueueUseCase(get(), get(), get()) }

    // Book use case
    single { ReadBookByIdUseCase(get()) }
    single { CreateBookUseCase(get(), get(), get(), get()) }
    single { UpdateBookUseCase(get(), get(), get(), get()) }
    single { DeleteBookUseCase(get()) }
    single { ReadBookByBbkUseCase(get()) }
    single { ReadBookByPublisherUseCase(get()) }
    single { ReadBookByAuthorUseCase(get()) }
    single { ReadBookBySentenceUseCase(get(), get()) }
    single { ReadBooksUseCase(get()) }

    // Favorite use case
    single { CreateFavoriteUseCase(get(), get(), get()) }
    single { DeleteFavoriteUseCase(get()) }
    single { ReadFavoriteByUserIdUseCase(get()) }
}