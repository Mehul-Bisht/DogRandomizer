package com.example.dograndomizer.data.repository

import com.example.dograndomizer.data.remote.DogApi
import com.example.dograndomizer.domain.models.Dog
import com.example.dograndomizer.domain.repository.DogRepository
import com.example.dograndomizer.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import toDog
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by Mehul Bisht on 24-10-2021
 */

class DogRepositoryImpl @Inject constructor(
    private val api: DogApi
) : DogRepository {

    private val _dogs: MutableStateFlow<List<Resource<Dog>>> = MutableStateFlow(listOf())
    private var limit = MutableStateFlow(-1)
    private var counter = MutableStateFlow(-1)
    private val _isLoading = MutableStateFlow(false)

    @ExperimentalCoroutinesApi
    val isEmpty = counter.flatMapLatest {
        flow {
            emit(it < 1)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun isBackStackEmpty(): Flow<Boolean> = isEmpty

    @ExperimentalCoroutinesApi
    val loader = _isLoading.flatMapLatest {
        flow {
            emit(it)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun isLoading(): Flow<Boolean> = loader

    override suspend fun getPrevious(): Flow<Resource<Dog>> = flow {
        decrementCount()
        emit(_dogs.value[counter.value])
    }

    override suspend fun getNext(): Flow<Resource<Dog>> = flow {
        if (counter.value < limit.value) {
            incrementCount()
            emit(_dogs.value[counter.value])
        } else {
            getDogs().collect {
                emit(it)
            }
        }
    }

    private suspend fun getDogs()
            : Flow<Resource<Dog>> = flow {
        try {
            incrementCountAndLimit()
            startLoading()
            val preLoad = Resource.Loading<Dog>()
            emit(preLoad)
            val mutableList1 = _dogs.value.toMutableList()
            mutableList1.add(preLoad)
            _dogs.value = mutableList1

            val data = api.getRandomDog()
            val response = Resource.Success(data.toDog())

            val mutableList = _dogs.value.toMutableList()
            mutableList[counter.value] = response
            _dogs.value = mutableList
            stopLoading()
            emit(response)

        } catch (e: Exception) {
            val error = Resource.Error<Dog>(e.message.toString())
            val mutableList = _dogs.value.toMutableList()
            mutableList[counter.value] = error
            stopLoading()
            emit(error)
        }
    }

    private fun startLoading() {
        _isLoading.value = true
    }

    private fun stopLoading() {
        _isLoading.value = false
    }

    private fun incrementCountAndLimit() {
        incrementCount()
        incrementLimit()
    }

    private fun incrementCount() {
        counter.value = counter.value + 1
    }

    private fun decrementCount() {
        counter.value = counter.value - 1
    }

    private fun incrementLimit() {
        limit.value = limit.value + 1
    }
}