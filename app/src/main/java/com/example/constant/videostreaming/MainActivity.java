package com.example.constant.videostreaming;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


/**
 * Created by Constant on 6/28/2016.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button videoInbuilt, videoCustom, videoZoom, videoTexture;
    DisplayMetrics displaySize, realSize;
    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        //mainLayout.getBackground().setAlpha();

        videoInbuilt = (Button)findViewById(R.id.videoInbuilt);
        videoInbuilt.setOnClickListener(this);

        videoCustom = (Button)findViewById(R.id.videoCustom);
        videoCustom.setOnClickListener(this);

        videoZoom = (Button)findViewById(R.id.videoZoom);
        videoZoom.setOnClickListener(this);

        videoTexture = (Button)findViewById(R.id.videoTexture);
        videoTexture.setOnClickListener(this);

        displaySize = new DisplayMetrics();
        realSize = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaySize);
        getWindowManager().getDefaultDisplay().getRealMetrics(realSize);

        Log.e("Width==",""+displaySize.widthPixels);
        Log.e("Height==",""+displaySize.heightPixels);
        Log.e("RealWidth==",""+realSize.widthPixels);
        Log.e("RealHeight==",""+realSize.heightPixels);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.videoZoom)
        {
            startActivity(new Intent(this, VideoZoomActivity.class));
        }
        else if(v.getId()==R.id.videoCustom)
        {
            startActivity(new Intent(this, CustomVideoActivity.class));
        }
        else if(v.getId()==R.id.videoInbuilt)
        {
            startActivity(new Intent(this, SampleVideoRun.class));
        }
        else if(v.getId()==R.id.videoTexture)
        {
            startActivity(new Intent(this, UseTextureVideoView.class));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            getWindowManager().getDefaultDisplay().getMetrics(displaySize);
            getWindowManager().getDefaultDisplay().getRealMetrics(realSize);

            Log.e("Width==",""+displaySize.widthPixels);
            Log.e("Height==",""+displaySize.heightPixels);
            Log.e("RealWidth==",""+realSize.widthPixels);
            Log.e("RealHeight==",""+realSize.heightPixels);


        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            getWindowManager().getDefaultDisplay().getMetrics(displaySize);
            getWindowManager().getDefaultDisplay().getRealMetrics(realSize);

            Log.e("Width==",""+displaySize.widthPixels);
            Log.e("Height==",""+displaySize.heightPixels);
            Log.e("RealWidth==",""+realSize.widthPixels);
            Log.e("RealHeight==",""+realSize.heightPixels);

        }
    }
}
