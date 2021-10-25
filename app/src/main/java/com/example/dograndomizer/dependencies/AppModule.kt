package com.example.dograndomizer.dependencies

import com.example.dograndomizer.BuildConfig.BASE_URL
import com.example.dograndomizer.data.remote.DogApi
import com.example.dograndomizer.data.repository.DogRepositoryImpl
import com.example.dograndomizer.dispatcher.CoroutineDispatcherProvider
import com.example.dograndomizer.domain.repository.DogRepository
import com.example.dograndomizer.domain.use_cases.*
import com.example.dograndomizer.presentation.random_dog.RandomDogViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Mehul Bisht on 24-10-2021
 */

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideDogApi(retrofit: Retrofit): DogApi {
        return retrofit.create(DogApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDogRepository(dogApi: DogApi): DogRepository {
        return DogRepositoryImpl(dogApi)
    }

    @Provides
    @Singleton
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

    @Provides
    @Singleton
    fun providesGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}