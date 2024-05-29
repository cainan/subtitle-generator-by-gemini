package com.cso.subtitlegenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.cso.subtitlegenerator.ui.screen.HomeScreen
import com.cso.subtitlegenerator.ui.theme.SubtitleGeneratorTheme
import com.cso.subtitlegenerator.ui.viewmodel.HomeViewModel
import com.google.ai.client.generativeai.GenerativeModel


class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<HomeViewModel>()

        enableEdgeToEdge()
        setContent {
            SubtitleGeneratorTheme {
                HomeScreen(viewModel = viewModel, onGenerateClicked = {
                    viewModel.generateSubtitle()
                })
            }
        }
    }
}
