package com.example.dograndomizer.presentation.random_dog

import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.dograndomizer.data.repository.FakeDogRepository
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import javax.inject.Inject
import javax.inject.Named
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.dograndomizer.R
import com.example.dograndomizer.data.dummy_data.DummyDogs
import com.example.dograndomizer.dispatcher.FakeCoroutineDispatcherProvider
import com.example.dograndomizer.domain.use_cases.GetBackStackStatusUseCase
import com.example.dograndomizer.domain.use_cases.GetLoadingStatusUseCase
import com.example.dograndomizer.domain.use_cases.GetNextDogUseCase
import com.example.dograndomizer.domain.use_cases.GetPreviousDogUseCase
import org.hamcrest.Matchers.not

/**
 * Created by Mehul Bisht on 24-10-2021
 */

@HiltAndroidTest
class RandomDogActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Rule(order = 1) @JvmField
    val activityScenarioRule: ActivityScenarioRule<RandomDogActivity> =
        ActivityScenarioRule(RandomDogActivity::class.java)

    @Inject
    lateinit var fakeDogRepository: FakeDogRepository

    @Inject
    @Named("FakeGetBackStackStatusUseCase")
    lateinit var getBackStackStatusUseCase: GetBackStackStatusUseCase

    @Inject
    @Named("FakeGetLoadingStatusUseCase")
    lateinit var getLoadingStatusUseCase: GetLoadingStatusUseCase

    @Inject
    @Named("FakeGetNextDogUseCase")
    lateinit var getNextDogUseCase: GetNextDogUseCase

    @Inject
    @Named("FakeGetPreviousDogUseCase")
    lateinit var getPreviousDogUseCase: GetPreviousDogUseCase

    @Inject
    @Named("FakeRandomDogViewModelFactory")
    lateinit var viewModelFactory: RandomDogViewModelFactory

    private lateinit var testCoroutineDispatcher: CoroutineDispatcher
    private lateinit var viewModel: RandomDogViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        hiltRule.inject()
        testCoroutineDispatcher =
            FakeCoroutineDispatcherProvider.testCoroutineDispatcher
        Dispatchers.setMain(testCoroutineDispatcher)

        activityScenarioRule.scenario.onActivity { activity ->
            viewModel = ViewModelProvider(
                activity,
                viewModelFactory
            ).get(RandomDogViewModel::class.java)
        }
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun checkPreviousButtonStateOnAppStartup_ShouldBeDisabled() {
        onView(withId(R.id.btn_previous)).check(matches(not(isEnabled())))
    }

    @Test
    fun testAppStartup_FirstItemIsLoaded() {
        runBlocking {
            val job = launch {
                val expectedId = DummyDogs.getDogById(1).id
                val foundId = (viewModel.dogState.value as RandomDogViewModel.DogState.Success).data.id
                assertThat(foundId).isEqualTo(expectedId)
            }
            job.join()
            job.cancel()
        }
    }

    @Test
    fun clickNextButton_SecondItemIsLoaded() {

        onView(withId(R.id.btn_next)).perform(click())

        runBlocking {
            val job = launch {
                val expectedId = DummyDogs.getDogById(2).id
                val foundId = (viewModel.dogState.value as RandomDogViewModel.DogState.Success).data.id
                assertThat(foundId).isEqualTo(expectedId)
            }
            job.join()
            job.cancel()
        }
    }

    @Test
    fun clickNextButton_PreviousButtonGetsEnabled() {
        onView(withId(R.id.btn_next)).perform(click())
        onView(withId(R.id.btn_previous)).check(matches(isEnabled()))
    }

    @Test
    fun clickNextButtonFollowedByPreviousButton_BackStackIsMaintained() {
        runBlocking {

            onView(withId(R.id.btn_next)).perform(click())
            onView(withId(R.id.btn_previous)).perform(click())

            val job = launch {
                val expectedId = DummyDogs.getDogById(1).id
                val foundId = (viewModel.dogState.value as RandomDogViewModel.DogState.Success).data.id
                assertThat(foundId).isEqualTo(expectedId)
            }
            job.join()
            job.cancel()
        }
    }
}