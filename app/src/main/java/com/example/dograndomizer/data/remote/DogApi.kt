package com.example.dograndomizer.data.remote

import com.example.dograndomizer.data.remote.dto.DogDto
import retrofit2.http.GET

/**
 * Created by Mehul Bisht on 24-10-2021
 */

interface DogApi {

    @GET(".")
    suspend fun getRandomDog(): DogDto
}