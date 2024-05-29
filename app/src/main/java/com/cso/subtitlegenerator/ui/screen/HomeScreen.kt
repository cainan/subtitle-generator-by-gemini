package com.cso.subtitlegenerator.ui.screen

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cso.subtitlegenerator.MainActivity
import com.cso.subtitlegenerator.R
import com.cso.subtitlegenerator.ui.theme.SubtitleGeneratorTheme
import com.cso.subtitlegenerator.ui.uistate.HomeUiState
import com.cso.subtitlegenerator.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel, onGenerateClicked: () -> Unit = {}) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreen(uiState, onGenerateClicked)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(uiState: HomeUiState, onGenerateClicked: () -> Unit = {}) {

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> uiState.onImageUriChanged(uri) }
    )

    fun launchPhotoPicker() {
        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.app_name)) })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
//            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Escolha uma imagem para ser analisada")
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = uiState.imageUri,
                contentDescription = null,
//                placeholder = BitmapPainter(ImageBitmap.imageResource(id = R.drawable.ic_launcher_background)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clickable {
                        Log.d(MainActivity.TAG, "AsyncImageClicked")
                        launchPhotoPicker()
                    },
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.humor,
                onValueChange = {
                    uiState.onHumorChanged(it)
                },
                label = { Text("Descreva o humor da legenda") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.humor,
                onValueChange = {
                    TODO()
                },
                label = { Text("") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    onGenerateClicked()
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Gerar Legenda")
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SubtitleGeneratorTheme {
        HomeScreen(HomeViewModel())
    }
}
