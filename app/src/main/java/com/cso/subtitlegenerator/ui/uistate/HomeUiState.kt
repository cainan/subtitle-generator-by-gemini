package com.cso.subtitlegenerator.ui.uistate

import android.net.Uri

data class HomeUiState(
    var humor: String = "",
    val onHumorChanged: (String) -> Unit = {},
    var imageUri: Uri? = null,
    val onImageUriChanged: (Uri?) -> Unit = {},
    val generatedSubtitle: String = "",
    var isLoading : Boolean = false,
) {
    fun canGenerate(): Boolean {
        return imageUri != null
    }


}