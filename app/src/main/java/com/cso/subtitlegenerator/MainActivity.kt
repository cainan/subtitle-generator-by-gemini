package com.cso.subtitlegenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.cso.subtitlegenerator.ui.screen.HomeScreen
import com.cso.subtitlegenerator.ui.theme.SubtitleGeneratorTheme
import com.cso.subtitlegenerator.ui.viewmodel.HomeViewModel


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
                HomeScreen(context = this, viewModel = viewModel, onGenerateClicked = {
                    viewModel.generateSubtitle()
                })
            }
        }
    }
}
