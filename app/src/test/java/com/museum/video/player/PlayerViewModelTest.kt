package com.museum.video.player

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.exoplayer2.SimpleExoPlayer
import com.museum.video.TestDispatcher
import com.museum.video.data.Response
import com.museum.video.data.models.remote.VideoRemote
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
        val video = VideoRemote("", "", "", url, "1")

        runBlocking {
            whenever(videoRepo.getVideoIds())
                .thenReturn(Response.Success(arrayListOf("1", "2", "3")))

            whenever(videoRepo.getVideo("1"))
                .thenReturn(Response.Success(video))
        }

        viewModel.getVideoIds()
        val actual = (viewModel.action.value?.get() as PlayerAction.ShowVideo).url
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
        val actual = (viewModel.action.value?.get() as? PlayerAction.ShowError)?.e
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
        val actual = (viewModel.action.value?.get() as? PlayerAction.ShowError)?.e
        assertEquals(error, actual)
    }

    @Test
    fun `Next Video With Rollover`() {
        val url1 = "url1"
        val url2 = "url2"
        val url3 = "url3"
        val video1 = VideoRemote("", "", "", url1, "1")
        val video2 = VideoRemote("", "", "", url2, "2")
        val video3 = VideoRemote("", "", "", url3, "3")

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
        var actual = (viewModel.action.value?.get() as PlayerAction.ShowVideo).url
        assertEquals(url1, actual)

        viewModel.nextVideo()
        actual = (viewModel.action.value?.get() as PlayerAction.ShowVideo).url
        assertEquals(url2, actual)

        viewModel.nextVideo()
        actual = (viewModel.action.value?.get() as PlayerAction.ShowVideo).url
        assertEquals(url3, actual)

        viewModel.nextVideo()
        actual = (viewModel.action.value?.get() as PlayerAction.ShowVideo).url
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