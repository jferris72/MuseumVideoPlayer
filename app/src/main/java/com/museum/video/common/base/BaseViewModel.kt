package com.museum.video.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.museum.video.data.Action
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext


abstract class BaseViewModel<A>(
    private val dispatcher: BaseDispatcher
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = dispatcher.default

    val action = MutableLiveData<Action<A>>()

    fun action(a: A) {
        action.postValue(Action(a))
    }

}