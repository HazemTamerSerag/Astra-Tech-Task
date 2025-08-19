package com.example.app.data.model

import com.google.gson.annotations.SerializedName

data class Blog(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("content")
    val content: String,
    
    @SerializedName("photo")
    val photo: String,
    
    @SerializedName("created_at")
    val createdAt: String,
    
    @SerializedName("updated_at")
    val updatedAt: String
) {
    val fullPhotoUrl: String
        get() = if (photo.startsWith("http")) photo else "http://taskapi.astra-tech.net/storage/$photo"
}
