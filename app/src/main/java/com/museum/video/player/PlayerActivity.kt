package com.museum.video.player

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.museum.video.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class PlayerActivity : AppCompatActivity(), Player.EventListener {

    private val viewModel: PlayerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupState()
    }

    private fun setupState() {
        viewModel.state.observe(this, Observer {
            if(it != null) onRender(it)
        })
    }

    private fun onRender(state: PlayerState) {
        if(exoPlayer.player == null && state.player != null) {
            exoPlayer.player = state.player
        } else if(exoPlayer.player == null && state.videoUrl != null) {
            createPlayer(state.videoUrl).also {
                viewModel.setPlayer(it)
            }
        } else if(state.videoUrl != null) {
            exoPlayer.player.prepare(createMediaSource(state.videoUrl))
        }
    }

    private fun createPlayer(url: String): SimpleExoPlayer {
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        val player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

        player.addListener(this)
        player.prepare(createMediaSource(url))
        player.playWhenReady = true

        return player
    }

    private fun createMediaSource(url: String): ExtractorMediaSource {
        val userAgent = Util.getUserAgent(this, getString(R.string.app_name))

        return ExtractorMediaSource(
            Uri.parse(url),
            DefaultDataSourceFactory(this, userAgent),
            DefaultExtractorsFactory(),
            null,
            null
        )
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when(playbackState) {
            Player.STATE_ENDED -> {
                viewModel.nextVideo()
            }
        }
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        viewModel.playerError(error)
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
    }

    override fun onSeekProcessed() {
    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
    }

    override fun onLoadingChanged(isLoading: Boolean) {
    }

    override fun onPositionDiscontinuity(reason: Int) {
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
    }
}
