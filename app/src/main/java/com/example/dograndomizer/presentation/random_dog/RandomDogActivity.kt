package com.example.dograndomizer.presentation.random_dog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.dograndomizer.R
import com.example.dograndomizer.databinding.ActivityRandomDogBinding
import com.example.dograndomizer.extensions.disable
import com.example.dograndomizer.extensions.enable
import com.example.dograndomizer.extensions.gone
import com.example.dograndomizer.extensions.visible
import com.example.dograndomizer.presentation.random_dog.RandomDogViewModel.DogState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class RandomDogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRandomDogBinding

    @Inject
    lateinit var viewModelFactory: RandomDogViewModelFactory
    private val viewModel: RandomDogViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandomDogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            viewModel.getNextDog()
        }

        binding.btnPrevious.setOnClickListener {
            viewModel.getPreviousDog()
        }

        lifecycleScope.launchWhenStarted {

            launch {
                viewModel.dogState.collect { state ->
                    when (state) {
                        is DogState.Loading -> {
                            binding.progress.visible()
                        }
                        is DogState.Success -> {
                            binding.progress.gone()
                            Glide.with(binding.root)
                                .load(state.data.imageUrl)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .error(R.drawable.ic_error_load)
                                .into(binding.image)
                        }
                        is DogState.Error -> {
                            binding.progress.gone()
                            Snackbar.make(binding.root, state.message ?: "an unknown error occurred",Snackbar.LENGTH_SHORT).show()
                        }
                        is DogState.Initial -> Unit
                    }
                }
            }

            launch {
                viewModel.status.collect {
                    if (!it) {
                        binding.btnNext.enable()
                    } else {
                        binding.btnNext.disable()
                    }
                }
            }

            launch {
                viewModel.isBackStackEmpty.collect {
                    if (it) {
                        binding.btnPrevious.disable()
                    } else {
                        binding.btnPrevious.enable()
                    }
                }
            }
        }
    }
}