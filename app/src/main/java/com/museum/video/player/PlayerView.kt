package com.museum.video.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.museum.video.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class PlayerView : AppCompatActivity() {

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

    }

}
