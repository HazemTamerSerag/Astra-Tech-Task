package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.ui.component.NetworkDialog
import com.example.app.ui.navigation.BlogNavigation
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.viewmodel.BlogViewModel
import com.example.app.util.NetworkMonitor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val viewModel: BlogViewModel = viewModel()
                val context = LocalContext.current
                val networkMonitor = remember { NetworkMonitor(context) }
                var isOnline by remember { mutableStateOf(true) }

                DisposableEffect(Unit) {
                    networkMonitor.start()
                    onDispose {}
                }

                LaunchedEffect(Unit) {
                    networkMonitor.isConnected.collect { isConnected ->
                        isOnline = isConnected
                        if (isConnected) {
                            viewModel.loadBlogs()
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BlogNavigation()

                    if (!isOnline) {
                        NetworkDialog(
                            onRetry = {
                                if (isOnline) {
                                    viewModel.loadBlogs()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}