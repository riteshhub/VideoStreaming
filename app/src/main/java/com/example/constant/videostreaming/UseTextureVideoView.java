package com.example.constant.videostreaming;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

//import com.devbrackets.android.exomedia.ui.widget.EMVideoView;

/**
 * Created by Constant on 7/8/2016.
 */
public class UseTextureVideoView extends AppCompatActivity {

    VideoView emVideoView;
    MediaController mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_video);

        emVideoView = (VideoView)findViewById(R.id.video_view);
        emVideoView.setVideoURI(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
        mc = new MediaController(this);
        emVideoView.setMediaController(mc);
        emVideoView.start();

    }

}
