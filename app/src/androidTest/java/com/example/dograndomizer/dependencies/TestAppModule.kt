package com.example.dograndomizer.dependencies

import com.example.dograndomizer.data.repository.FakeDogRepository
import com.example.dograndomizer.dispatcher.CoroutineDispatcherProvider
import com.example.dograndomizer.domain.repository.DogRepository
import com.example.dograndomizer.domain.use_cases.*
import com.example.dograndomizer.presentation.random_dog.RandomDogViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Mehul Bisht on 24-10-2021
 */

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideDogRepository(): DogRepository {
        return FakeDogRepository()
    }

    @Provides
    @Singleton
    @Named("FakeGetBackStackStatusUseCase")
    fun provideGetBackStackStatusUseCase(fakeDogRepository: DogRepository): GetBackStackStatusUseCase {
        return GetBackStackStatusUseCase(fakeDogRepository)
    }

    @Provides
    @Singleton
    @Named("FakeGetLoadingStatusUseCase")
    fun provideGetLoadingStatusUseCase(fakeDogRepository: DogRepository): GetLoadingStatusUseCase {
        return GetLoadingStatusUseCase(fakeDogRepository)
    }

    @Provides
    @Singleton
    @Named("FakeGetNextDogUseCase")
    fun provideGetNextDogUseCase(fakeDogRepository: DogRepository): GetNextDogUseCase {
        return GetNextDogUseCase(fakeDogRepository)
    }

    @Provides
    @Singleton
    @Named("FakeGetPreviousDogUseCase")
    fun provideGetPreviousDogUseCase(fakeDogRepository: DogRepository): GetPreviousDogUseCase {
        return GetPreviousDogUseCase(fakeDogRepository)
    }

    @Provides
    @Singleton
    @Named("FakeRandomDogViewModelFactory")
    fun providesViewModelFactory(
        getPreviousDogUseCase: GetPreviousDogUseCase,
        getNextDogUseCase: GetNextDogUseCase,
        getLoadingStatusUseCase: GetLoadingStatusUseCase,
        getBackStackStatusUseCase: GetBackStackStatusUseCase,
        coroutineDispatcherProvider: CoroutineDispatcherProvider
    ): RandomDogViewModelFactory {
        return RandomDogViewModelFactory(
            getPreviousDogUseCase,
            getNextDogUseCase,
            getLoadingStatusUseCase,
            getBackStackStatusUseCase,
            coroutineDispatcherProvider
        )
    }
}