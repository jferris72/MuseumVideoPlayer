package com.museum.video.common.base

import kotlinx.coroutines.CoroutineDispatcher


interface BaseDispatcher {
    val default: CoroutineDispatcher
}