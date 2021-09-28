package com.mubarakansari.exo_video_player_in_kotlin

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : AppCompatActivity() {
    private var simpleExoPlayer: ExoPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar?.hide()
        setContentView(R.layout.activity_video)



        img_thumbnail.setOnClickListener {
            exoPlayerVideo()
        }
        val img = "https://i.ytimg.com/vi/aqz-KE-bpKQ/maxresdefault.jpg"
        Glide.with(this).load(img).into(img_thumbnail)

        exo_fullscreen_icon.setOnClickListener {
            if (this.checkLandscapeOrientation()) {
                this.changeOrientationToLandscape(false)

            } else {
                this.changeOrientationToLandscape(true)

            }
        }

    }

    private fun changeOrientationToLandscape(shouldLandscape: Boolean) {
        requestedOrientation = if (shouldLandscape) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

    }

    private fun checkLandscapeOrientation(): Boolean {
        val orientation = resources.configuration.orientation
        return orientation == Configuration.ORIENTATION_LANDSCAPE
    }


    private fun exoPlayerVideo() {
        this.changeOrientationToLandscape(true)
        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        epPlayer.player = simpleExoPlayer
        val dataSourceFactory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, getString(R.string.app_name))
        )

        //val VIDEO_URL = "asset:///test.mp4"
        val VIDEO_URL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(VIDEO_URL))
        simpleExoPlayer!!.prepare(videoSource)
        simpleExoPlayer!!.playWhenReady = true
        simpleExoPlayer!!.videoComponent
        simpleExoPlayer!!.addListener(object : Player.EventListener {


            override fun onPlayerStateChanged(
                playWhenReady: Boolean,
                playbackState: Int
            ) {
                if (playbackState == Player.STATE_READY) {
                    progress_circular.visibility = View.GONE
                    epPlayer.visibility = View.VISIBLE
                    exo_fullscreen_icon.visibility = View.VISIBLE
                    img_thumbnail.visibility = View.GONE
                    img_thumbnail_time.visibility = View.GONE
                }
                if (playbackState == Player.STATE_BUFFERING) {
                    progress_circular.visibility = View.VISIBLE
                    exo_fullscreen_icon.visibility = View.GONE
                }
            }
        })

    }

    override fun onDestroy() {
        simpleExoPlayer!!.stop()
        super.onDestroy()
    }
}