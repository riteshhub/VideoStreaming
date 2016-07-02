package com.example.constant.videostreaming;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


/**
 * Created by Constant on 6/28/2016.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button startVideo, startCustomVideo, sampleVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startVideo = (Button)findViewById(R.id.startVideo);
        startVideo.setOnClickListener(this);

        startCustomVideo = (Button)findViewById(R.id.startVideo);
        startCustomVideo.setOnClickListener(this);

        sampleVideo = (Button)findViewById(R.id.sampleVideo);
        sampleVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.startVideo)
        {
            startActivity(new Intent(this, VideoPlayerActivity.class));
        }
        else if(v.getId()==R.id.startCustomVideo)
        {
            startActivity(new Intent(this, CustomVideoActivity.class));
        }
        else if(v.getId()==R.id.sampleVideo)
        {
            startActivity(new Intent(this, SampleVideoRun.class));
        }
    }
}
