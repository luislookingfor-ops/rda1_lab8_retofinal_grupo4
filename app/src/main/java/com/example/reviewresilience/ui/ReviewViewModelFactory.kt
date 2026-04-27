package com.example.reviewresilience.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.reviewresilience.data.ReviewPreferences

class ReviewViewModelFactory(
    private val application: Application,
    private val reviewPreferences: ReviewPreferences
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewViewModel(
                stateHandle = androidx.lifecycle.SavedStateHandle(),
                reviewPrefs = reviewPreferences
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}