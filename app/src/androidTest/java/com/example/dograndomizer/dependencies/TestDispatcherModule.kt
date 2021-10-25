package com.example.dograndomizer.dependencies

import com.example.dograndomizer.dispatcher.CoroutineDispatcherProvider
import com.example.dograndomizer.dispatcher.FakeCoroutineDispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Mehul Bisht on 24-10-2021
 */

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatcherModule::class]
)
object TestDispatcherModule {

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun providesCoroutineDispatcher(): CoroutineDispatcherProvider {
        return FakeCoroutineDispatcherProvider()
    }
}