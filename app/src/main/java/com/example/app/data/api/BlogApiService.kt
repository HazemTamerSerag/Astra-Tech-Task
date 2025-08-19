package com.example.app.data.api

import com.example.app.data.model.Blog
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface BlogApiService {
    
    @GET("api/blogs")
    suspend fun getAllBlogs(): Response<List<Blog>>
    
    @GET("api/blogs/show/{id}")
    suspend fun getBlogById(@Path("id") id: Int): Response<Blog>
    
    @Multipart
    @POST("api/blogs/store")
    suspend fun createBlog(
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part photo: MultipartBody.Part?
    ): Response<Blog>
    
    @Multipart
    @POST("api/blogs/update/{id}")
    suspend fun updateBlog(
        @Path("id") id: Int,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part photo: MultipartBody.Part?
    ): Response<Blog>
    
    @POST("api/blogs/delete/{id}")
    suspend fun deleteBlog(@Path("id") id: Int): Response<Unit>
}
