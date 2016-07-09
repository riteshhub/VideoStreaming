package com.example.constant.videostreaming;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by Constant on 7/2/2016.
 */


public class SampleVideoRun extends AppCompatActivity implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnInfoListener, View.OnClickListener {

    VideoView video;
    MediaController mediaController;
    ProgressBar spinner;
    FrameLayout frameLayout;
    FrameLayout.LayoutParams lpp, mRootParam;
    DisplayMetrics dm, vm;
    ImageButton videoState;
    Bitmap videoImage;
    String videoURL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    Uri uri;
    int original_video_width, original_video_height;
    boolean completed, zoomState = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sample_video);
        ColorDrawable drawable = new ColorDrawable(Color.argb(100, 0, 0, 0));
        getSupportActionBar().setBackgroundDrawable(drawable);
        getSupportActionBar().setTitle("Big Bunny");

        frameLayout = (FrameLayout) findViewById(R.id.root_view);
        mRootParam = (ScrollView.LayoutParams) frameLayout.getLayoutParams();


        spinner = (ProgressBar) findViewById(R.id.my_spinner);
        spinner.setVisibility(View.INVISIBLE);

        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            lpp = new FrameLayout.LayoutParams(dm.widthPixels, 170);
            lpp.gravity = Gravity.BOTTOM;
            lpp.setMargins(0, 0, 0, 100);
        } else {
            lpp = new FrameLayout.LayoutParams(dm.widthPixels, 170);
        }

        videoState = (ImageButton) findViewById(R.id.playpause);

        video = (VideoView) findViewById(R.id.videoSurface);
        video.setVideoPath(videoURL);
        //video.setVideoPath("http://54.210.75.226/black_telly/upload/1467822979Property_24_--_03_July_2016_--2_4.3gp");

        original_video_width = video.getWidth();
        original_video_height = video.getHeight();

        video.setMediaController(mediaController = new MediaController(this) {
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
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
        mediaController.setAlpha(0.6f);
        mediaController.setVisibility(View.INVISIBLE);


        hideSystemUI();
        video.setOnCompletionListener(this);
        video.setOnInfoListener(this);
        videoState.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.zoom) {
            ZoomToggle(item);
        } else if (id == R.id.screenshot) {
            videoImage = getVideoFrame(videoURL);

            if(videoImage != null)
                storeImage(videoImage);
            else
                Log.d("ZeroFile","Unable to capture frame");
        }
        return super.onOptionsItemSelected(item);
    }

    void ZoomToggle(MenuItem item) {
        if (zoomState == false) {
            item.setIcon(R.mipmap.ic_zoom_in);
            getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            video.getHolder().setFixedSize(dm.widthPixels + (dm.widthPixels / 2), dm.heightPixels + (dm.heightPixels / 2));

            mRootParam.width = dm.widthPixels + (dm.widthPixels / 2);
            mRootParam.height = dm.heightPixels + (dm.heightPixels / 2);

            zoomState = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            Log.d("ZoomState", zoomState + "");
        } else {
            item.setIcon(R.mipmap.ic_zoom_out);
            getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            video.getHolder().setFixedSize(dm.widthPixels, dm.heightPixels);

            mRootParam.width = dm.widthPixels;
            mRootParam.height = dm.heightPixels;

            zoomState = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            Log.d("ZoomState", zoomState + "");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.playpause && completed == false) {
            videoState.setVisibility(View.GONE);
            mediaController.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            video.start();
            Log.d("Started", "Hello");
        } else {
            videoState.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            video.start();
            Log.d("Resumed", "Hello");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            lpp = new FrameLayout.LayoutParams(dm.widthPixels, 170);
            mediaController.setLayoutParams(lpp);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            lpp = new FrameLayout.LayoutParams(dm.widthPixels, 170);
            lpp.gravity = Gravity.BOTTOM;
            lpp.setMargins(0, 0, 0, 100);
            mediaController.setLayoutParams(lpp);
        }
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        mRootParam.width = dm.widthPixels;
        mRootParam.height = dm.heightPixels;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        video.pause();
        completed = true;
        mp.seekTo(0);
        videoState.setImageResource(R.mipmap.ic_replay);
        videoState.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {

        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            spinner.setVisibility(View.GONE);
            return true;
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            spinner.setVisibility(View.VISIBLE);
            return false;
        } else {
            spinner.setVisibility(View.GONE);
            return true;
        }
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // When the window loses focus (e.g. the action overflow is shown),
        // cancel any pending hide action. When the window gains focus,
        // hide the system UI.
        if (hasFocus) {
            delayedHide(200);
            Log.e("hasFOCUS===", "");
            //titleVideo.setVisibility(View.VISIBLE);

        } else {
            mHideHandler.removeMessages(0);
            //titleVideo.setVisibility(View.GONE);
            Log.d("NoFOCUS===", "");
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
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private final Handler mHideHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            hideSystemUI();
            Log.d("NoFOCUS===", "");
        }
    };

    private void delayedHide(int delayMillis) {
        mHideHandler.removeMessages(0);
        mHideHandler.sendEmptyMessageDelayed(0, delayMillis);
    }

    /**
     * Capture Video frame for screenshot
     **/
    public Bitmap getVideoFrame(String url) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //uri = Uri.parse(url);
            Log.d("111","22222");
            if (Build.VERSION.SDK_INT >= 14)
                retriever.setDataSource(url, new HashMap<String, String>());
            else
                retriever.setDataSource(url);
            return retriever.getFrameAtTime(video.getCurrentPosition());
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
            }
        }
        return null;
    }

    /**
     * Create a File for saving an image or video
     */
    private void storeImage(Bitmap image) {

        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String mImageName = "MI_" + timeStamp + ".jpg";

        if(isExternalStorageWritable()==true)
        {
            Log.d("State","Writable");
        }
        else
        {
            Log.d("State","NonWritable");
        }


        if(isExternalStorageReadable()==true)
        {
            Log.d("State","Readable");
        }
        else
        {
            Log.d("State","NonReadable");
        }

        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File mediaStorageDir = new File(root + "/VideoStreaming");
        mediaStorageDir.mkdir();

        File file = new File(mediaStorageDir, mImageName);

        if (file.exists()) {
            file.delete();
            Log.d("FilePath",file.getPath());
        }
        try {
            file.createNewFile();
            Log.d("FilePath",file.getPath());
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            MediaScannerConnection.scanFile(this, new String[] { file.getPath() }, new String[] { "image/jpeg" }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
