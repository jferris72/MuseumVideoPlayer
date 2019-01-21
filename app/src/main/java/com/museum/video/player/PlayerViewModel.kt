package com.museum.video.player

import androidx.lifecycle.MutableLiveData
import com.museum.video.common.base.BaseDispatcher
import com.museum.video.common.base.BaseViewModel
import com.museum.video.data.video.VideoDataSource
import kotlinx.coroutines.launch


class PlayerViewModel(
    dispatcher: BaseDispatcher,
    private val videoDataSource: VideoDataSource
) : BaseViewModel(dispatcher) {

    val state = MutableLiveData<PlayerState>()
    private var videoIds: List<String> = listOf()

    init {
        getVideoIds()
    }

    private fun getVideoIds() = launch {
        val videoIds = videoDataSource.getVideoIds()
    }

}