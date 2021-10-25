package com.example.dograndomizer.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

/**
 * Created by Mehul Bisht on 24-10-2021
 */

@ExperimentalCoroutinesApi
open class FakeCoroutineDispatcherProvider : CoroutineDispatcherProvider {
    companion object {
        val testCoroutineDispatcher = TestCoroutineDispatcher()
    }
    override val main: CoroutineDispatcher by lazy { testCoroutineDispatcher }
    override val io: CoroutineDispatcher by lazy { testCoroutineDispatcher }
    override val default: CoroutineDispatcher by lazy { testCoroutineDispatcher }
    override val unconfirmed: CoroutineDispatcher by lazy { testCoroutineDispatcher }
}