package com.museum.video.data.video

import com.museum.video.data.AppApi
import com.museum.video.data.Response


class VideoRepository(
    private val appApi: AppApi
) : VideoDataSource {

    override suspend fun getVideoIds() = try {
        val apiResponse = appApi.getVideoIds().await()
        Response.Success(apiResponse.ids)
    } catch(e: Exception) {
        Response.Error(e)
    }

    override suspend fun getVideo(id: String) = try {
        val apiResponse = appApi.getVideo(id).await()
        Response.Success(apiResponse.videoList[0])
    } catch(e: Exception) {
        Response.Error(e)
    }
}