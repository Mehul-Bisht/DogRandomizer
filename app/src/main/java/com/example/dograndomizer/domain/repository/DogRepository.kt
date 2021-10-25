package com.example.dograndomizer.domain.repository

import com.example.dograndomizer.domain.models.Dog
import com.example.dograndomizer.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Created by Mehul Bisht on 24-10-2021
 */

interface DogRepository {

    suspend fun getPrevious(): Flow<Resource<Dog>>

    suspend fun getNext(): Flow<Resource<Dog>>

    suspend fun isLoading(): Flow<Boolean>

    suspend fun isBackStackEmpty(): Flow<Boolean>
}