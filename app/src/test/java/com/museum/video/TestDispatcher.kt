package com.museum.video

import com.museum.video.common.base.BaseDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


class TestDispatcher(
    override val default: CoroutineDispatcher = Dispatchers.Unconfined
) : BaseDispatcher