package com.cso.subtitlegenerator.ui.viewmodel

import android.app.Application
import android.graphics.ImageDecoder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cso.subtitlegenerator.BuildConfig
import com.cso.subtitlegenerator.ui.uistate.HomeUiState
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "HomeViewModel"
    }

    private val cr = application.contentResolver
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState>
        get() = _uiState.asStateFlow()

    private val harassmentSafety =
        SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.LOW_AND_ABOVE)
    private val hateSpeechSafety =
        SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.LOW_AND_ABOVE)
    private val gemini = GenerativeModel(
        // The Gemini 1.5 models are versatile and work with most use cases
        modelName = "gemini-1.5-flash",
        // Access your API key as a Build Configuration variable (see "Set up your API key" above)
        apiKey = BuildConfig.apiKey,
        safetySettings = listOf(
            harassmentSafety, hateSpeechSafety
        )
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
            _uiState.value = _uiState.value.copy(
                isLoading = true,
            )

            val source =
                ImageDecoder.createSource(cr, uiState.value.imageUri!!)
            val image = ImageDecoder.decodeBitmap(source)

            val inputContent = content {
                image(image)
                text(
                    "Você é um influenciador." +
                            "Crie uma legenda para a imagem indicada usando um tom de humor ${uiState.value.humor}," +
                            "esta foto será postada em uma plataforma digital"
                )
            }

            var fullResponse = ""
            gemini.generateContentStream(inputContent).collect { chunk ->
                print(chunk.text)
                fullResponse += chunk.text
            }

            Log.d(TAG, "Response: $fullResponse")

            _uiState.value = _uiState.value.copy(
                generatedSubtitle = fullResponse,
                isLoading = false,
                isPopupDisplayed = true,
            )
        }
    }

    fun onDialogClicked(isDisplayed: Boolean) {
        _uiState.value = _uiState.value.copy(
            isPopupDisplayed = isDisplayed,
        )
    }
}