package com.museum.video.data.video

import com.museum.video.data.AppApi
import com.museum.video.data.Response
import com.museum.video.data.models.local.Video
import com.museum.video.data.models.remote.VideoRemote


class VideoRepository(
    private val appApi: AppApi
) : VideoDataSource {

    private val videos: HashMap<String, Video> = hashMapOf()

    override suspend fun getVideoIds() = try {
        val apiResponse = appApi.getVideoIds().await()
        if(apiResponse.ids == null) Response.Success(arrayListOf())
        else Response.Success(apiResponse.ids)
    } catch(e: Exception) {
        Response.Error(e)
    }

    override suspend fun getVideo(id: String) = try {
        if(videos.contains(id)) {
            Response.Success(videos[id] as Video)
        } else {
            val apiResponse = appApi.getVideo(id).await()
            if(apiResponse.videoList?.get(0)?.url == null) {
                Response.Error(Throwable("Video not found"))
            }
            else {
                val video = Video(apiResponse.videoList[0].url as String)
                videos[id] = video
                Response.Success(video)
            }
        }
    } catch(e: Exception) {
        Response.Error(e)
    }
}