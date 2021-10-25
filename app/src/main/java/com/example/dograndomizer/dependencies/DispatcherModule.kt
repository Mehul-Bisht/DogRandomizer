package com.example.dograndomizer.dependencies

import com.example.dograndomizer.dispatcher.CoroutineDispatcherProvider
import com.example.dograndomizer.dispatcher.RealCoroutineDispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Mehul Bisht on 24-10-2021
 */

@Module
@InstallIn(SingletonComponent::class)
class DispatcherModule {

    @Provides
    @Singleton
    fun providesCoroutineDispatcher(): CoroutineDispatcherProvider {
        return RealCoroutineDispatcherProvider()
    }
}
