package com.example.reviewresilience.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.reviewresilience.data.ReviewPreferences
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val stateHandle: SavedStateHandle,
    private val reviewPrefs: ReviewPreferences
) : ViewModel() {

    // Puntaje: sobrevive solo a rotación (ViewModel)
    var rating by mutableIntStateOf(0)
        private set

    fun updateRating(newRating: Int) {
        rating = newRating
    }

    // Comentario: sobrevive a muerte de proceso (SavedStateHandle)
    var comment by mutableStateOf(stateHandle.get<String>("comment_key") ?: "")
        private set

    fun updateComment(newComment: String) {
        comment = newComment
        stateHandle["comment_key"] = newComment
    }

    // Borrador: sobrevive a cierre total (DataStore)
    val draftFromDisk = reviewPrefs.draftContent.asLiveData()
    val ratingFromDisk = reviewPrefs.savedRating.asLiveData()

    fun saveDraft(content: String) {
        viewModelScope.launch {
            reviewPrefs.saveDraft(content)
        }
    }

    fun saveRatingToDisk(rating: Int) {
        viewModelScope.launch {
            reviewPrefs.saveRating(rating)
        }
    }
}