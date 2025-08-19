package com.example.app.ui.dialog

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.app.data.model.Blog
import com.example.app.ui.viewmodel.BlogViewModel
import java.io.File
import java.io.FileOutputStream

@Composable
fun CreateEditBlogDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    blogToEdit: Blog? = null,
    viewModel: BlogViewModel = viewModel()
) {
    val context = LocalContext.current

    var title by rememberSaveable { mutableStateOf(blogToEdit?.title ?: "") }
    var content by rememberSaveable { mutableStateOf(blogToEdit?.content ?: "") }
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> selectedImageUri = uri }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(isVisible) {
        if (isVisible) {
            title = blogToEdit?.title ?: ""
            content = blogToEdit?.content ?: ""
            selectedImageUri = null
        }
    }

    LaunchedEffect(Unit) {
        viewModel.operationCompleted.collect { success ->
            if (success) {
                onDismiss()
            }
        }
    }

    if (isVisible) {
        Dialog(onDismissRequest = { if (!isLoading) onDismiss() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = if (blogToEdit == null) "Create New Blog" else "Edit Blog",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(
                                enabled = !isLoading,
                                onClick = { imagePickerLauncher.launch("image/*") }
                            )
                            .alpha(if (isLoading) 0.7f else 1f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Selected image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else if (blogToEdit?.photo != null) {
                            AsyncImage(
                                model = blogToEdit.fullPhotoUrl,
                                contentDescription = "Current blog image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text("Tap to select an image")
                        }

                        if (isLoading) {
                            CircularProgressIndicator()
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        isError = errorMessage?.contains("title") == true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Content") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        maxLines = 5,
                        enabled = !isLoading,
                        isError = errorMessage?.contains("content") == true
                    )

                    errorMessage?.let { message ->
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { if (!isLoading) onDismiss() },
                            enabled = !isLoading
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                val imageFile =
                                    selectedImageUri?.let { uri -> uriToFile(context, uri) }
                                if (blogToEdit == null) {
                                    if (selectedImageUri != null) {
                                        viewModel.createBlog(title, content, imageFile)
                                    }
                                } else {
                                    viewModel.updateBlog(
                                        blogToEdit.id,
                                        title,
                                        content,
                                        imageFile
                                    )
                                }
                            },
                            enabled = title.isNotBlank() &&
                                    content.isNotBlank() &&
                                    !isLoading &&
                                    (blogToEdit != null || selectedImageUri != null)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(if (blogToEdit != null) "Update" else "Create")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun uriToFile(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(tempFile)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        tempFile
    } catch (e: Exception) {
        null
    }
}
