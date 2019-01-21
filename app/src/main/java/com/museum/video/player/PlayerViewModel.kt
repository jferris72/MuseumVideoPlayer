package com.museum.video.player

import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.SimpleExoPlayer
import com.museum.video.common.base.BaseDispatcher
import com.museum.video.common.base.BaseViewModel
import com.museum.video.data.Response
import com.museum.video.data.video.VideoDataSource
import kotlinx.coroutines.launch


class PlayerViewModel(
    dispatcher: BaseDispatcher,
    private val videoDataSource: VideoDataSource
) : BaseViewModel<PlayerAction>(dispatcher) {

    var player: SimpleExoPlayer? = null
    private var videoIds: ArrayList<String> = arrayListOf()
    private var currentVideoIndex = 0

    init {
        getVideoIds()
    }

    fun getVideoIds() = launch {
        val response = videoDataSource.getVideoIds()
        when(response) {
            is Response.Success -> {
                videoIds = response.data
                getVideo()
            }
            is Response.Error -> {
                action(PlayerAction.ShowError(response.e))
            }
        }
    }

    fun getVideo() = launch {
        if(currentVideoIndex >= videoIds.size) currentVideoIndex = 0

        val response = videoDataSource.getVideo(videoIds[currentVideoIndex])
        when(response) {
            is Response.Success -> action(PlayerAction.ShowVideo(response.data.url))
            is Response.Error -> action(PlayerAction.ShowError(response.e))
        }
    }

    fun nextVideo() {
        currentVideoIndex += 1
        getVideo()
    }

    fun playerError() {
        nextVideo()
    }

    override fun onCleared() {
        super.onCleared()
        player?.release()
        player = null
    }

}