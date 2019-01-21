package com.museum.video.data.video

import com.museum.video.data.AppApi
import com.museum.video.data.Response
import com.museum.video.data.models.Video


class VideoRepository(
    private val appApi: AppApi
) : VideoDataSource {

    val videos: HashMap<String, Video> = hashMapOf()

    override suspend fun getVideoIds() = try {
        val apiResponse = appApi.getVideoIds().await()
        Response.Success(apiResponse.ids)
    } catch(e: Exception) {
        Response.Error(e)
    }

    override suspend fun getVideo(id: String) = try {
        if(videos.contains(id)) {
            Response.Success(videos[id] as Video)
        } else {
            val apiResponse = appApi.getVideo(id).await()
            videos[id] = apiResponse.videoList[0]
            Response.Success(apiResponse.videoList[0])
        }
    } catch(e: Exception) {
        Response.Error(e)
    }
}