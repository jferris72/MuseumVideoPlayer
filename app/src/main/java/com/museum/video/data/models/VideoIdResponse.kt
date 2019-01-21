package com.museum.video.data.models

import com.google.gson.annotations.SerializedName


data class VideoIdResponse(
    @SerializedName("media_items") val ids: List<String>
)