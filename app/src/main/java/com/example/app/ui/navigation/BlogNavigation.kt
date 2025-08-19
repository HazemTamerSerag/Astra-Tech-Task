package com.example.app.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.ui.dialog.CreateEditBlogDialog
import com.example.app.ui.screen.BlogDetailScreen
import com.example.app.ui.screen.BlogListScreen
import com.example.app.ui.viewmodel.BlogViewModel

@Composable
fun BlogNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: BlogViewModel = viewModel()
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var blogToEdit by remember { mutableStateOf<com.example.app.data.model.Blog?>(null) }
    
    NavHost(
        navController = navController,
        startDestination = "blog_list"
    ) {
        composable("blog_list") {
            BlogListScreen(
                onBlogClick = { blog ->
                    navController.navigate("blog_detail/${blog.id}")
                },
                onCreateBlogClick = {
                    showCreateDialog = true
                },
                viewModel = viewModel
            )
        }
        
        composable("blog_detail/{blogId}") { backStackEntry ->
            val blogId = backStackEntry.arguments?.getString("blogId")?.toIntOrNull() ?: 0
            BlogDetailScreen(
                blogId = blogId,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = { blog ->
                    blogToEdit = blog
                    showCreateDialog = true
                },
                viewModel = viewModel
            )
        }
    }
    
    CreateEditBlogDialog(
        isVisible = showCreateDialog,
        onDismiss = {
            showCreateDialog = false
            blogToEdit = null
        },
        blogToEdit = blogToEdit,
        viewModel = viewModel
    )
}
