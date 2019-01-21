package com.museum.video.data.models

//TODO make remote nullable
data class Video(
    val quality: String,
    val duration: String,
    val name: String,
    val url: String,
    val id: String
)