package com.museum.video.data.video

import com.museum.video.data.Response
import com.museum.video.data.models.Video


interface VideoDataSource {

    suspend fun getVideoIds(): Response<List<String>>
    suspend fun getVideo(id: String): Response<Video>

}