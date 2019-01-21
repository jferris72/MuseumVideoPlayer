package com.museum.video.data.models


data class Video(
    val quality: String,
    val duration: String,
    val name: String,
    val url: String,
    val id: String,
    var failed: Boolean = false
)