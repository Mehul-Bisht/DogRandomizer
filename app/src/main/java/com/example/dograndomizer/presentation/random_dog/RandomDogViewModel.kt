package com.example.dograndomizer.presentation.random_dog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dograndomizer.dispatcher.CoroutineDispatcherProvider
import com.example.dograndomizer.domain.models.Dog
import com.example.dograndomizer.domain.use_cases.*
import com.example.dograndomizer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Mehul Bisht on 24-10-2021
 */

@HiltViewModel
class RandomDogViewModel @Inject constructor(
    private val getPreviousDogUseCase: GetPreviousDogUseCase,
    private val getNextDogUseCase: GetNextDogUseCase,
    private val getLoadingStatusUseCase: GetLoadingStatusUseCase,
    private val getBackStackStatusUseCase: GetBackStackStatusUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val _dogState: MutableStateFlow<DogState> = MutableStateFlow(DogState.Initial)
    val dogState: StateFlow<DogState> get() = _dogState

    private val _status: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val status: StateFlow<Boolean> get() = _status

    private val _isBackStackEmpty: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isBackStackEmpty: StateFlow<Boolean> get() = _isBackStackEmpty

    sealed class DogState {
        object Initial : DogState()
        object Loading : DogState()
        data class Success(val data: Dog) : DogState()
        data class Error(val message: String?) : DogState()
    }

    init {
        getNextDog()
        getStatus()
        getBackStackStatus()
    }

    private fun getStatus() {
        viewModelScope.launch {
            getLoadingStatusUseCase().collect {
                _status.value = it
            }
        }
    }

    private fun getBackStackStatus() {
        viewModelScope.launch {
            getBackStackStatusUseCase().collect {
                _isBackStackEmpty.value = it
            }
        }
    }

    fun getPreviousDog() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            getPreviousDogUseCase().collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        _dogState.value = DogState.Loading
                    }
                    is Resource.Success -> {
                        _dogState.value = DogState.Success(state.data!!)
                    }
                    is Resource.Error -> {
                        _dogState.value = DogState.Error(state.message)
                    }
                }
            }
        }
    }

    fun getNextDog() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            getNextDogUseCase().collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        _dogState.value = DogState.Loading
                    }
                    is Resource.Success -> {
                        _dogState.value = DogState.Success(state.data!!)
                    }
                    is Resource.Error -> {
                        _dogState.value = DogState.Error(state.message)
                    }
                }
            }
        }
    }

//    fun getPreviousDog() {
//        if (counter.value > 0) {
//            decrementCount()
//            _dogState.value = DogState.Success(data.value[counter.value].data!!)
//        }
//    }
//
//    fun getNextDog() {
//        viewModelScope.launch(coroutineDispatcherProvider.io) {
//            getRandomDogUseCase().collect {
//                val list = data.value.toMutableList()
//                list.add(it)
//                data.value = list
//                incrementCount()
//            }
//
//            getRandomDogUseCase().collect { state ->
//                when (state) {
//                    is Resource.Loading -> {
//                        _dogState.value = DogState.Loading
//                    }
//                    is Resource.Success -> {
//                        _dogState.value = DogState.Success(state.data!!)
//                    }
//                    is Resource.Error -> {
//                        _dogState.value = DogState.Error(state.message)
//                    }
//                }
//            }
//        }
//    }
}