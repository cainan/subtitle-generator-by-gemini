package com.cso.subtitlegenerator.ui.screen

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cso.subtitlegenerator.MainActivity
import com.cso.subtitlegenerator.R
import com.cso.subtitlegenerator.ui.component.CustomAlertDialog
import com.cso.subtitlegenerator.ui.theme.SubtitleGeneratorTheme
import com.cso.subtitlegenerator.ui.uistate.HomeUiState
import com.cso.subtitlegenerator.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    context: Context?,
    viewModel: HomeViewModel,
    onGenerateClicked: () -> Unit = {},
    onDialogClicked: (isDisplayed: Boolean) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreen(context, uiState, onGenerateClicked, onDialogClicked)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    context: Context?,
    uiState: HomeUiState,
    onGenerateClicked: () -> Unit = {},
    onDialogClicked: (isDisplayed: Boolean) -> Unit = {},
) {

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

        CustomAlertDialog(
            context = context!!,
            text = uiState.generatedSubtitle,
            showDialog = uiState.isPopupDisplayed,
            onDismiss = {
                onDialogClicked(false)
            })

        Column(
            modifier = Modifier
                .fillMaxSize(1F)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .weight(0.95f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = stringResource(id = R.string.choose_img_text))

                Spacer(modifier = Modifier.height(16.dp))

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(uiState.imageUri)
                        .crossfade(true).build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(2.dp))
                        .clickable {
                            Log.d(MainActivity.TAG, "AsyncImageClicked")
                            launchPhotoPicker()
                        },
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.upload_24px),
                    error = painterResource(R.drawable.upload_24px),
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.humor,
                    onValueChange = {
                        uiState.onHumorChanged(it)
                    },
                    maxLines = 2,
                    label = { Text(stringResource(id = R.string.describe_humor_text)) },
                    modifier = Modifier.fillMaxWidth()
                )

            }

            Button(
                onClick = {
                    onGenerateClicked()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                enabled = uiState.canGenerate(),

                ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        modifier = Modifier
                            .size(24.dp)
                    )
                } else {
                    Text(stringResource(id = R.string.generate_subtitle))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SubtitleGeneratorTheme {
        HomeScreen(context = null, HomeUiState(generatedSubtitle = "Test"))
    }
}
