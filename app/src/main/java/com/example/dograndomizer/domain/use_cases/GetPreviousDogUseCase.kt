package com.example.dograndomizer.domain.use_cases

import com.example.dograndomizer.domain.models.Dog
import com.example.dograndomizer.domain.repository.DogRepository
import com.example.dograndomizer.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Mehul Bisht on 25-10-2021
 */

class GetPreviousDogUseCase @Inject constructor(
    private val repository: DogRepository
) {
    suspend operator fun invoke()
            : Flow<Resource<Dog>> = flow {
        repository.getPrevious().collect {
            emit(it)
        }
    }
}