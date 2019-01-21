package com.museum.video.player


sealed class PlayerAction {

    class ShowError(val e: Throwable) : PlayerAction()
    class ShowVideo(val url: String) : PlayerAction()

}