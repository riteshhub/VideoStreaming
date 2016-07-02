package com.example.constant.videostreaming;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;


public class VideoPlayerActivity extends AppCompatActivity implements View.OnSystemUiVisibilityChangeListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnInfoListener
{

    VideoView video;
    MediaController mediaController;
    View mDecorView;
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        //dm.widthPixels

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        FrameLayout.LayoutParams lpp = new FrameLayout.LayoutParams(
                dm.widthPixels, FrameLayout.LayoutParams.MATCH_PARENT);

        video = (VideoView)findViewById(R.id.videoSurface);
        video.setVideoPath("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");

        video.setMediaController(mediaController = new MediaController(this){
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        return true;
                    } else if (event.getAction() == KeyEvent.ACTION_UP) {
                        ((Activity) getContext()).onBackPressed();
                        return true;
                    }
                }
                return super.dispatchKeyEvent(event);
            }
        });
        mediaController.setLayoutParams(lpp);
        mediaController.setAlpha(0.5f);
        mDecorView = getWindow().getDecorView();
        mDecorView.setOnSystemUiVisibilityChangeListener(this);
        //video.setLayoutParams(new FrameLayout.LayoutParams(dm.widthPixels, dm.heightPixels));
        hideSystemUI();
        video.start();

        video.setOnCompletionListener(this);
        video.setOnInfoListener(this);
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.seekTo(0);
    }


    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {

        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
        {
            spinner.setVisibility(View.GONE);
            return true;
        }
        else if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
        {
            spinner.setVisibility(View.VISIBLE);
            return false;
        }
        else
        {
            spinner.setVisibility(View.GONE);
            return true;
        }
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // When the window loses focus (e.g. the action overflow is shown),
        // cancel any pending hide action. When the window gains focus,
        // hide the system UI.
        if (hasFocus) {
            delayedHide(200);
            //titleVideo.setVisibility(View.VISIBLE);

        } else {
            mHideHandler.removeMessages(0);
            //titleVideo.setVisibility(View.GONE);
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
    }
    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
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
}