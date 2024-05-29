package com.cso.subtitlegenerator.ui.viewmodel

import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cso.subtitlegenerator.BuildConfig
import com.cso.subtitlegenerator.ui.uistate.HomeUiState
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
    }

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState>
        get() = _uiState.asStateFlow()

    val gemini = GenerativeModel(
        // The Gemini 1.5 models are versatile and work with most use cases
        modelName = "gemini-1.5-flash",
        // Access your API key as a Build Configuration variable (see "Set up your API key" above)
        apiKey = BuildConfig.apiKey
    )

    init {
        _uiState.update { state ->
            state.copy(
                onHumorChanged = {
                    _uiState.value = _uiState.value.copy(
                        humor = it
                    )
                },
                onImageUriChanged = { uri ->
                    uri?.let {
                        _uiState.value = _uiState.value.copy(
                            imageUri = it
                        )
                    }
                }
            )
        }
    }

    fun generateSubtitle() {
        viewModelScope.launch {
            Log.d(TAG, "Generate Subtitle")

//            val image =
//                MediaStore.Images.Media.getBitmap(coroutineContext., uiState.value.imageUri);
//
//            val inputContent = content {
//                image(image)
//
//                text("Descreva imagem com humor ${uiState.value.humor}")
//            }
//            var fullResponse = ""
//            gemini.generateContentStream(inputContent).collect { chunk ->
//                print(chunk.text)
//                fullResponse += chunk.text
//            }
        }
    }
}