package com.example.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.model.Blog
import com.example.app.data.repository.BlogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class BlogViewModel : ViewModel() {

    private val repository = BlogRepository()

    private val _blogs = MutableStateFlow<List<Blog>>(emptyList())
    val blogs: StateFlow<List<Blog>> = _blogs.asStateFlow()

    private val _selectedBlog = MutableStateFlow<Blog?>(null)
    val selectedBlog: StateFlow<Blog?> = _selectedBlog.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _operationCompleted = MutableStateFlow(false)
    val operationCompleted = _operationCompleted.asStateFlow()

    init {
        loadBlogs()
    }

    private fun resetOperationState() {
        _operationCompleted.value = false
        _errorMessage.value = null
    }

    fun loadBlogs() {
        viewModelScope.launch {
            _isLoading.value = true
            resetOperationState()

            try {
                val response = repository.getAllBlogs()
                if (response.isSuccessful) {
                    _blogs.value = response.body() ?: emptyList()
                    _operationCompleted.value = true
                } else {
                    _errorMessage.value = "Failed to load blogs: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadBlogById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            resetOperationState()

            try {
                val response = repository.getBlogById(id)
                if (response.isSuccessful) {
                    _selectedBlog.value = response.body()
                    _operationCompleted.value = true
                } else {
                    _errorMessage.value = "Failed to load blog: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createBlog(title: String, content: String, photoFile: File?) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = repository.createBlog(title, content, photoFile)
                if (response.isSuccessful) {
                    loadBlogs() // Refresh the list
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = if (errorBody?.contains("photo field is required") == true) {
                        "Photo is required to create a blog post"
                    } else {
                        "Failed to create blog: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateBlog(id: Int, title: String, content: String, photoFile: File?) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = repository.updateBlog(id, title, content, photoFile)
                if (response.isSuccessful) {
                    loadBlogs() // Refresh the list
                    loadBlogById(id) // Refresh the selected blog
                } else {
                    _errorMessage.value = "Failed to update blog: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteBlog(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            resetOperationState()

            try {
                val response = repository.deleteBlog(id)
                if (response.isSuccessful) {
                    _operationCompleted.value = true
                    loadBlogs()
                } else {
                    _errorMessage.value = response.message() ?: "Failed to delete blog"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

}