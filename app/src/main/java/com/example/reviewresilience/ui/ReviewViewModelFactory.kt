package com.example.reviewresilience.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.reviewresilience.data.ReviewPreferences

class ReviewViewModelFactory(
    private val application: Application,
    private val reviewPreferences: ReviewPreferences
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            return ReviewViewModel(
                stateHandle = extras.createSavedStateHandle(),
                reviewPrefs = reviewPreferences
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}