package com.example.app.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.app.R

@Composable
fun NetworkDialog(
    onRetry: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(stringResource(R.string.no_internet_connection)) },
        text = { Text(stringResource(R.string.please_check_your_internet_connection)) },
        confirmButton = {
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }
        }
    )
}
