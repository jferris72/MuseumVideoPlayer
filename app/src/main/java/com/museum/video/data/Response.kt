package com.museum.video.data


sealed class Response<out T : Any> {

    data class Success<out T : Any>(val data: T) : Response<T>()
    data class Error(val e: Throwable) : Response<Nothing>()

}