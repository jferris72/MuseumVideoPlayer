package com.museum.video.data.models.remote

import com.google.gson.annotations.SerializedName


data class VideoIdResponse(
    @SerializedName("media_items") val ids: ArrayList<String>?
)