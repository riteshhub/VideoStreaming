package com.example.constant.videostreaming;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * Created by Constant on 7/4/2016.
 */
public class VideoZoomActivity extends AppCompatActivity implements View.OnTouchListener, MediaPlayer.OnPreparedListener, SeekBar.OnSeekBarChangeListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener{

    int MIN_WIDTH;
    private FrameLayout frameLayout;
    private FrameLayout.LayoutParams mRootParam;
    VodView video;
    ScaleGestureDetector mScaleGestureDetector;
    GestureDetector mGestureDetector;
    DisplayMetrics dm;
    SeekBar seekBar;
    ImageButton playpause;
    boolean play = false, completed=false;
    ProgressBar spinner;
    TextView startTime,endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_zoom);

        ColorDrawable drawable = new ColorDrawable(Color.argb(100,0,0,0));
        getSupportActionBar().setBackgroundDrawable(drawable);
        getSupportActionBar().setTitle("Big Bunny");

        video = (VodView)findViewById(R.id.vodView1);
        video.setVideoPath("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        video.setOnPreparedListener(this);
        video.setOnTouchListener(this);
        video.setOnCompletionListener(this);
        video.setOnInfoListener(this);

        seekBar = (SeekBar)findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(this);

        spinner = (ProgressBar)findViewById(R.id.my_spinner);
        spinner.setVisibility(View.INVISIBLE);

        playpause = (ImageButton)findViewById(R.id.playpause);
        playpause.setOnTouchListener(this);

        startTime = (TextView)findViewById(R.id.startTime);
        endTime = (TextView)findViewById(R.id.endTime);
        hideSystemUI();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {

        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {

        }
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {

        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
        {
            spinner.setVisibility(View.GONE);
            seekBar.postDelayed(onEverySecond, 1000);
            Log.d("Info 1","Rendering");
            return true;
        }
        else if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
        {
            spinner.setVisibility(View.VISIBLE);
            seekBar.removeCallbacks(onEverySecond);
            Log.d("Info 2","Buffering");
            return true;
        }
        else
        {
            spinner.setVisibility(View.GONE);
            seekBar.postDelayed(onEverySecond, 1000);
            Log.d("Info 3","Other");
            return true;
        }
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        seekBar.setMax(video.getDuration());
        endTime.setText(video.getDuration()/1000+"");
       // video.start();
        Log.e("AppXYZ OnPrepared==",video.getDuration()+"");

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser ) {
            // this is when actually seekbar has been seeked to a new position
            Log.d("AppXYZ ProgressChngd==","fds");
            video.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if((v.getId()==R.id.vodView1 || v.getId()==R.id.playpause) && event.getAction()==MotionEvent.ACTION_DOWN && spinner.getVisibility()!=View.VISIBLE)
        {

            if(play==false) {
                if(video.getDuration()<0) {
                    spinner.setVisibility(View.VISIBLE);
                    playpause.setVisibility(View.GONE);
                 }
                video.start();
                playpause.setVisibility(View.GONE);
                play=true;
                seekBar.postDelayed(onEverySecond, 1000);
                Log.e("VideoState","Playing"+play);
            }
            else
            {
                video.pause();
                playpause.setVisibility(View.VISIBLE);
                playpause.setImageResource(R.mipmap.ic_pause);
                seekBar.removeCallbacks(onEverySecond);
                play=false;
                Log.e("VideoState","Pause"+play);
            }
        }
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        video.seekTo(0);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // When the window loses focus (e.g. the action overflow is shown),
        // cancel any pending hide action. When the window gains focus,
        // hide the system UI.
        if (hasFocus) {
            delayedHide(200);
            Log.d("hasFOCUS===","");
            //titleVideo.setVisibility(View.VISIBLE);

        } else {
            mHideHandler.removeMessages(0);
            //titleVideo.setVisibility(View.GONE);
            Log.d("NoFOCUS===","");
            getSupportActionBar().show();
        }
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getSupportActionBar().hide();
        seekBar.setVisibility(View.INVISIBLE);

    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private final Handler mHideHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            hideSystemUI();
        }
    };

    private void delayedHide(int delayMillis) {
        mHideHandler.removeMessages(0);
        mHideHandler.sendEmptyMessageDelayed(0, delayMillis);
    }

    private Runnable onEverySecond=new Runnable() {
        @Override
        public void run() {

            if(seekBar != null) {
                seekBar.setProgress(video.getCurrentPosition());
                startTime.setText(video.getCurrentPosition()/1000+"");
            }

            if(video.isPlaying()) {
                seekBar.postDelayed(onEverySecond, 1000);
                Log.e("AppXYZ Not Null===",video.getCurrentPosition()+"");
            }
            else
            {
                seekBar.removeCallbacks(onEverySecond);
                Log.e("AppXYZ Null===",video.getCurrentPosition()+"");
            }
        }
    };
}
