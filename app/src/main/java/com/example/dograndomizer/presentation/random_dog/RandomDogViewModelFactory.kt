package com.example.dograndomizer.presentation.random_dog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dograndomizer.dispatcher.CoroutineDispatcherProvider
import com.example.dograndomizer.domain.use_cases.*
import javax.inject.Inject

/**
 * Created by Mehul Bisht on 24-10-2021
 */

class RandomDogViewModelFactory @Inject constructor(
    private val getPreviousDogUseCase: GetPreviousDogUseCase,
    private val getNextDogUseCase: GetNextDogUseCase,
    private val getLoadingStatusUseCase: GetLoadingStatusUseCase,
    private val getBackStackStatusUseCase: GetBackStackStatusUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RandomDogViewModel::class.java)) {
            return RandomDogViewModel(
                getPreviousDogUseCase,
                getNextDogUseCase,
                getLoadingStatusUseCase,
                getBackStackStatusUseCase,
                coroutineDispatcherProvider
            ) as T
        }
        throw IllegalArgumentException("ViewModel class not supported")
    }
}