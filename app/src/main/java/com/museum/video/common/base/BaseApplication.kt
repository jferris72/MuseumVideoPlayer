package com.museum.video.common.base

import android.app.Application
import com.museum.video.common.appModule
import org.koin.android.ext.android.startKoin


class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule))
    }
}