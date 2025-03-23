package com.cso.subtitlegenerator.ui.component

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.cso.subtitlegenerator.R

@Composable
fun CustomAlertDialog(
    context: Context,
    text: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,

    ) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(stringResource(id = R.string.generated_subtitle))
            },
            text = {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())) {
                    Text(
                        text = text,
                        modifier = Modifier.selectable(
                            selected = true,
                            enabled = true,
                            role = null,
                            onClick = {
                                val clipboardManager =
                                    context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

                                val clipData =
                                    ClipData.newPlainText("Subtitle copied", text)

                                clipboardManager.setPrimaryClip(clipData)

                                Toast.makeText(
                                    context,
                                    R.string.subtitle_copied,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            })
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onDismiss()
                }) {
                    Text("Ok")
                }
            },
        )
    }
}