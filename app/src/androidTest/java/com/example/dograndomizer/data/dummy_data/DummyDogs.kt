package com.example.dograndomizer.data.dummy_data

import com.example.dograndomizer.domain.models.Dog

/**
 * Created by Mehul Bisht on 25-10-2021
 */

object DummyDogs {

    private val Dog1 = Dog(
        imageUrl = "https://images.dog.ceo/breeds/spaniel-brittany/n02101388_4002.jpg",
        id = 1
    )

    private val Dog2 = Dog(
        imageUrl = "https://images.dog.ceo/breeds/boxer/n02108089_1072.jpg",
        id = 2
    )

    private val Dog3 = Dog(
        imageUrl = "https://images.dog.ceo/breeds/brabancon/n02112706_1307.jpg",
        id = 3
    )

    private val dogs: List<Dog> = listOf(Dog1, Dog2, Dog3)

    fun getDogById(id: Int): Dog {
        return dogs.find { id == it.id }!!
    }
}