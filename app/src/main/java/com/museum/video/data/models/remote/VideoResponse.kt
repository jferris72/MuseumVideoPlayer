package com.museum.video.data.models.remote

import com.google.gson.annotations.SerializedName


data class VideoResponse(
    @SerializedName("id") val videoList: List<VideoRemote>?
)