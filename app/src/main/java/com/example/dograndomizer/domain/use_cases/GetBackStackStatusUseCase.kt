package com.example.dograndomizer.domain.use_cases

import com.example.dograndomizer.domain.models.Dog
import com.example.dograndomizer.domain.repository.DogRepository
import com.example.dograndomizer.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Mehul Bisht on 24-10-2021
 */

class GetBackStackStatusUseCase @Inject constructor(
    private val dogRepository: DogRepository
) {
    suspend operator fun invoke()
            : Flow<Boolean> = flow {
        dogRepository.isBackStackEmpty().collect {
            emit(it)
        }
    }
}