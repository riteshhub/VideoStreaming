package com.example.constant.videostreaming;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

/**
 * Created by Constant on 7/1/2016.
 */
public class CustomVideoActivity extends AppCompatActivity {

    ProgressBar spinner;
    VideoView video;
    MediaControllerClass mc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide Status Bar, Navigation Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Hide Status Bar, Navigation Bar

        setContentView(R.layout.activity_video_player);

        spinner = (ProgressBar)findViewById(R.id.my_spinner);
        ColorDrawable drawable = new ColorDrawable(Color.argb(100,0,0,0));
        getSupportActionBar().setBackgroundDrawable(drawable);
        getSupportActionBar().setTitle("Big Bunny");

        video = (VideoView)findViewById(R.id.videoSurface);
        video.setVideoPath("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");

        mc = new MediaControllerClass(this);
        video.setMediaController(mc);
        video.start();
    }
}
