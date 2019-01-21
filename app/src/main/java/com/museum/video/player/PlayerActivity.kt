package com.museum.video.player

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
import com.museum.video.data.Action
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class PlayerActivity : AppCompatActivity(), Player.EventListener {

    private val viewModel: PlayerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPlayer()
        setupAction()
    }

    private fun setupAction() {
        viewModel.action.observe(this, Observer {
            if(it != null) onAction(it)
        })
    }

    private fun setupPlayer() {
        if(exoPlayer.player == null && viewModel.player != null) {
            exoPlayer.player = viewModel.player
        } else {
            exoPlayer.player = createPlayer().also { viewModel.player = it }
        }
    }

    private fun onAction(action: Action<PlayerAction>) {
        val playerAction = action.get()
        when(playerAction) {
            is PlayerAction.ShowError -> showError(playerAction.e)
            is PlayerAction.ShowVideo -> showVideo(playerAction.url)
        }
    }

    private fun showError(e: Throwable) {
        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
    }

    private fun showVideo(url: String) {
        exoPlayer.player?.prepare(createMediaSource(url))
    }

    private fun createPlayer(): SimpleExoPlayer {
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        val player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

        player.addListener(this)
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
            Player.STATE_ENDED -> viewModel.nextVideo()
        }
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        viewModel.playerError()
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
