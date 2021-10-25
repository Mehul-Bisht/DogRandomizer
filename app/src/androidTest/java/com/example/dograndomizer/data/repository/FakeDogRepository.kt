package com.example.dograndomizer.data.repository

import com.example.dograndomizer.data.dummy_data.DummyDogs
import com.example.dograndomizer.dispatcher.FakeCoroutineDispatcherProvider
import com.example.dograndomizer.domain.models.Dog
import com.example.dograndomizer.domain.repository.DogRepository
import com.example.dograndomizer.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Mehul Bisht on 24-10-2021
 */

class FakeDogRepository @Inject constructor(

) : DogRepository {

    private val _dogs: MutableStateFlow<List<Resource<Dog>>> = MutableStateFlow(listOf())
    private var limit = MutableStateFlow(-1)
    private var counter = MutableStateFlow(-1)
    private val _isLoading = MutableStateFlow(false)
    private var index = 1

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

    @ExperimentalCoroutinesApi
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

    @ExperimentalCoroutinesApi
    private suspend fun getDogs()
            : Flow<Resource<Dog>> = flow<Resource<Dog>> {

        incrementCountAndLimit()
        startLoading()
        val preLoad = Resource.Loading<Dog>()
        emit(preLoad)
        val mutableList1 = _dogs.value.toMutableList()
        mutableList1.add(preLoad)
        _dogs.value = mutableList1

        val data = DummyDogs.getDogById(index)
        index++
        val response = Resource.Success(data)

        val mutableList = _dogs.value.toMutableList()
        mutableList[counter.value] = response
        _dogs.value = mutableList
        stopLoading()
        emit(response)

    }.flowOn(FakeCoroutineDispatcherProvider.testCoroutineDispatcher)

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