package com.museum.video.data

import com.museum.video.data.models.remote.VideoIdResponse
import com.museum.video.data.models.remote.VideoResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path


interface AppApi {

    @GET("/media")
    fun getVideoIds(): Deferred<VideoIdResponse>

    @GET("/media/{id}")
    fun getVideo(
        @Path("id") id: String
    ): Deferred<VideoResponse>

}