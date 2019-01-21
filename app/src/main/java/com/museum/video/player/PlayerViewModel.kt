package com.museum.video.player

import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.museum.video.common.base.BaseDispatcher
import com.museum.video.common.base.BaseViewModel
import com.museum.video.data.Response
import com.museum.video.data.video.VideoDataSource
import kotlinx.coroutines.launch


class PlayerViewModel(
    dispatcher: BaseDispatcher,
    private val videoDataSource: VideoDataSource
) : BaseViewModel(dispatcher) {

    val state = MutableLiveData<PlayerState>()
    private var playerState = PlayerState()
        set(value) { state.postValue(value) }

    private var videoIds: ArrayList<String> = arrayListOf()
    private var currentVideo = 0

    init {
        getVideoIds()
    }

    private fun getVideoIds() = launch {
        val response = videoDataSource.getVideoIds()
        when(response) {
            is Response.Success -> {
                videoIds = response.data
                getVideo()
            }
            is Response.Error -> {
            }
        }
    }

    private fun getVideo() = launch {
        if(currentVideo >= videoIds.size) currentVideo = 0

        val response = videoDataSource.getVideo(videoIds[currentVideo])
        when(response) {
            is Response.Success -> {
                playerState = playerState.copy(videoUrl = response.data.url)
            }
            is Response.Error -> {
            }
        }
    }

    fun setPlayer(player: SimpleExoPlayer) {
        playerState = playerState.copy(player = player, videoUrl = "")
    }

    fun nextVideo() {
        currentVideo += 1
        getVideo()
    }

    fun playerError(error: ExoPlaybackException?) {
        nextVideo()
    }

    override fun onCleared() {
        super.onCleared()
        playerState.player?.release()
        playerState = playerState.copy(player = null)
    }

}