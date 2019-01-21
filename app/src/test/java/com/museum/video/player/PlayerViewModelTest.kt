package com.museum.video.player

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.exoplayer2.SimpleExoPlayer
import com.museum.video.common.TestDispatcher
import com.museum.video.data.Response
import com.museum.video.data.models.Video
import com.museum.video.data.video.VideoDataSource
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class PlayerViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val videoRepo: VideoDataSource = mock()
    private lateinit var viewModel: PlayerViewModel

    @Before
    fun setup() {
        viewModel = PlayerViewModel(TestDispatcher(), videoRepo)
    }

    @Test
    fun `Get Url Success`() {
        val url = "url"
        val video = Video("","","",url,"1")

        runBlocking {
            whenever(videoRepo.getVideoIds())
                .thenReturn(Response.Success(arrayListOf("1", "2", "3")))

            whenever(videoRepo.getVideo("1"))
                .thenReturn(Response.Success(video))
        }

        viewModel.getVideoIds()
        val actual = viewModel.state.value?.videoUrl
        assertEquals("url", actual)
    }

    @Test
    fun `Get Url Error`() {
        val error = Throwable("error")
        runBlocking {
            whenever(videoRepo.getVideoIds())
                .thenReturn(Response.Success(arrayListOf("1", "2", "3")))

            whenever(videoRepo.getVideo("1"))
                .thenReturn(Response.Error(error))
        }

        viewModel.getVideoIds()
        val actual = (viewModel.action.value?.get() as? PlayerAction.Error)?.e
        assertEquals(error, actual)
    }

    @Test
    fun `Get Ids Error`() {
        val error = Throwable("error")
        runBlocking {
            whenever(videoRepo.getVideoIds())
                .thenReturn(Response.Error(error))
        }

        viewModel.getVideoIds()
        val actual = (viewModel.action.value?.get() as? PlayerAction.Error)?.e
        assertEquals(error, actual)
    }

    @Test
    fun `Next Video With Rollover`() {
        val url1 = "url1"
        val url2 = "url2"
        val url3 = "url3"
        val video1 = Video("","","", url1,"1")
        val video2 = Video("", "", "", url2, "2")
        val video3 = Video("", "", "", url3, "3")

        runBlocking {
            whenever(videoRepo.getVideoIds())
                .thenReturn(Response.Success(arrayListOf("1", "2", "3")))

            whenever(videoRepo.getVideo("1"))
                .thenReturn(Response.Success(video1))

            whenever(videoRepo.getVideo("2"))
                .thenReturn(Response.Success(video2))

            whenever(videoRepo.getVideo("3"))
                .thenReturn(Response.Success(video3))
        }

        viewModel.getVideoIds()
        var actual = viewModel.state.value?.videoUrl
        assertEquals(url1, actual)

        viewModel.nextVideo()
        actual = viewModel.state.value?.videoUrl
        assertEquals(url2, actual)

        viewModel.nextVideo()
        assertEquals(url3, viewModel.state.value?.videoUrl)

        viewModel.nextVideo()
        actual = viewModel.state.value?.videoUrl
        assertEquals(url1, actual)
    }

    @Test
    fun `Set Player`() {
        val player: SimpleExoPlayer = mock()
        viewModel.setPlayer(player)

        val actual = viewModel.state.value?.player
        assertEquals(player, actual)
    }

}