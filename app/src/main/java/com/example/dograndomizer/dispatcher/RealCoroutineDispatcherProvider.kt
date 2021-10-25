package com.example.dograndomizer.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 * Created by Mehul Bisht on 24-10-2021
 */

open class RealCoroutineDispatcherProvider : CoroutineDispatcherProvider {
    override val main: CoroutineDispatcher by lazy { Dispatchers.Main }
    override val io: CoroutineDispatcher by lazy { Dispatchers.IO }
    override val default: CoroutineDispatcher by lazy { Dispatchers.Default }
    override val unconfirmed: CoroutineDispatcher by lazy { Dispatchers.Unconfined }
}
