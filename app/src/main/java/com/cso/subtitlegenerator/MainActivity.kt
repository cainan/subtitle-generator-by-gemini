package com.cso.subtitlegenerator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cso.subtitlegenerator.ui.screen.HomeScreen
import com.cso.subtitlegenerator.ui.theme.SubtitleGeneratorTheme
import com.google.ai.client.generativeai.GenerativeModel

class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val generativeModel = GenerativeModel(
            // The Gemini 1.5 models are versatile and work with most use cases
            modelName = "gemini-1.5-flash",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = BuildConfig.apiKey
        )
        enableEdgeToEdge()
        setContent {
            SubtitleGeneratorTheme {
                HomeScreen()
            }
        }
    }
}
