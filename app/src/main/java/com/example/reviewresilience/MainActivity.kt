package com.example.reviewresilience

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reviewresilience.data.ReviewPreferences
import com.example.reviewresilience.ui.ReviewViewModel
import com.example.reviewresilience.ui.ReviewViewModelFactory

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "CICLO_VIDA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        val reviewPreferences = ReviewPreferences(applicationContext)

        setContent {
            val viewModel: ReviewViewModel = viewModel(
                factory = ReviewViewModelFactory(application, reviewPreferences)
            )

            ReviewScreen(viewModel = viewModel)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}

@Composable
fun ReviewScreen(viewModel: ReviewViewModel) {
    val draftFromDisk by viewModel.draftFromDisk.observeAsState("")
    val ratingFromDisk by viewModel.ratingFromDisk.observeAsState(0)

    var draftInput by remember { mutableStateOf("") }
    var ratingInput by remember { mutableIntStateOf(ratingFromDisk) }

    // Cargar borrador del disco al iniciar
    LaunchedEffect(draftFromDisk) {
        if (draftInput.isEmpty() && draftFromDisk.isNotEmpty()) {
            draftInput = draftFromDisk
            viewModel.updateComment(draftFromDisk)
        }
    }

    // Predictive Back: bloquear si hay comentario
    BackHandler(enabled = viewModel.comment.isNotEmpty()) {
        Log.d("NAV", "El usuario intentó retroceder con un comentario en progreso")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Reseña de Producto",
            style = MaterialTheme.typography.headlineMedium
        )

        // Puntaje (Rating) con Slider
        Text("Puntaje: $ratingInput")
        Slider(
            value = ratingInput.toFloat(),
            onValueChange = {
                ratingInput = it.toInt()
                viewModel.updateRating(ratingInput)
            },
            valueRange = 1f..5f,
            steps = 3
        )

        // Comentario
        OutlinedTextField(
            value = viewModel.comment,
            onValueChange = { viewModel.updateComment(it) },
            label = { Text("Comentario (persiste al rotar/muerte proceso)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Botón Guardar Borrador (DataStore)
        Button(
            onClick = {
                viewModel.saveDraft(viewModel.comment)
                viewModel.saveRatingToDisk(ratingInput)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Guardar Borrador")
        }
    }
}