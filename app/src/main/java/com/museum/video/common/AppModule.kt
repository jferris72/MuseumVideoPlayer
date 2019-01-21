package com.museum.video.common

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.museum.video.BuildConfig
import com.museum.video.common.base.BaseDispatcher
import com.museum.video.data.AppApi
import com.museum.video.data.video.VideoDataSource
import com.museum.video.data.video.VideoRepository
import com.museum.video.player.PlayerViewModel
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val appModule = module {

    viewModel { PlayerViewModel(get(), get()) }

    single { Dispatcher() as BaseDispatcher }

    single { VideoRepository(get()) as VideoDataSource }

    single { createGson() }

    single { createConverterFactory(get()) }

    single { CoroutineCallAdapterFactory() }

    single { createOkHttp() }

    single { createRetrofit(get(), get(), get()) }

    single { createAppApi(get()) }

}

fun createGson() = GsonBuilder()
        .setLenient()
        .create()

fun createConverterFactory(gson: Gson) = GsonConverterFactory
    .create(gson)

fun createOkHttp() = OkHttpClient.Builder()
    .readTimeout(60L, TimeUnit.SECONDS)
    .connectTimeout(60L, TimeUnit.SECONDS)
    .build()

fun createRetrofit(
    okHttpClient: OkHttpClient,
    converterFactory: GsonConverterFactory,
    callAdapter: CoroutineCallAdapterFactory
) = Retrofit.Builder()
    .baseUrl(BuildConfig.API_URL)
    .client(okHttpClient)
    .addConverterFactory(converterFactory)
    .addCallAdapterFactory(callAdapter)
    .build()

fun createAppApi(
    retrofit: Retrofit
) = retrofit.create(AppApi::class.java)
