package com.museum.video.common

import com.museum.video.common.base.BaseDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


data class Dispatcher(
    override val default: CoroutineDispatcher = Dispatchers.IO
): BaseDispatcher