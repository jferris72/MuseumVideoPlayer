package com.museum.video.common

import com.museum.video.data.video.VideoDataSource
import com.museum.video.data.video.VideoRepository
import org.koin.dsl.module.module


val appModule = module {



    single { VideoRepository() as VideoDataSource }

}