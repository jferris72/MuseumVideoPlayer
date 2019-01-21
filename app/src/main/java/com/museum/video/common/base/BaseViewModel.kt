package com.museum.video.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext


abstract class BaseViewModel(
    private val dispatcher: BaseDispatcher
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = dispatcher.default

}