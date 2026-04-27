package com.example.reviewresilience.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "review_prefs")

class ReviewPreferences(private val context: Context) {

    companion object {
        val DRAFT_KEY = stringPreferencesKey("draft_content")
        val RATING_KEY = intPreferencesKey("saved_rating")
    }

    // Leer borrador desde disco
    val draftContent: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[DRAFT_KEY] ?: ""
    }

    // Leer puntaje guardado desde disco
    val savedRating: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[RATING_KEY] ?: 0
    }

    suspend fun saveDraft(content: String) {
        context.dataStore.edit { preferences ->
            preferences[DRAFT_KEY] = content
        }
    }

    // Guardar puntaje en disco
    suspend fun saveRating(rating: Int) {
        context.dataStore.edit { preferences ->
            preferences[RATING_KEY] = rating
        }
    }
}