package com.example.app.data.repository

import com.example.app.data.api.NetworkModule
import com.example.app.data.model.Blog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class BlogRepository {
    
    private val apiService = NetworkModule.blogApiService
    
    suspend fun getAllBlogs(): Response<List<Blog>> {
        return apiService.getAllBlogs()
    }
    
    suspend fun getBlogById(id: Int): Response<Blog> {
        return apiService.getBlogById(id)
    }
    
    suspend fun createBlog(title: String, content: String, photoFile: File?): Response<Blog> {
        val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val contentBody = content.toRequestBody("text/plain".toMediaTypeOrNull())
        
        val photoPart = photoFile?.let { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("photo", file.name, requestFile)
        }
        
        return apiService.createBlog(titleBody, contentBody, photoPart)
    }
    
    suspend fun updateBlog(id: Int, title: String, content: String, photoFile: File?): Response<Blog> {
        val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val contentBody = content.toRequestBody("text/plain".toMediaTypeOrNull())
        
        val photoPart = photoFile?.let { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("photo", file.name, requestFile)
        }
        
        return apiService.updateBlog(id, titleBody, contentBody, photoPart)
    }
    
    suspend fun deleteBlog(id: Int): Response<Unit> {
        return apiService.deleteBlog(id)
    }
}
