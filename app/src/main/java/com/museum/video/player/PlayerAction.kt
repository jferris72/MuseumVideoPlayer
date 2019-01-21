package com.museum.video.player


sealed class PlayerAction {

    class Error(val e: Throwable) : PlayerAction()

}