package com.museum.video.data


open class Action<out T>(private val content: T) {

    var handled = false
        private set

    fun get(): T? {
        return if(handled) {
            null
        } else {
            handled = true
            content
        }
    }

}