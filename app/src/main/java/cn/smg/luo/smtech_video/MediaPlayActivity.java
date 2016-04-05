package cn.smg.luo.smtech_video;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.smg.luo.smtech_video.widget.media.IRenderView;
import cn.smg.luo.smtech_video.widget.media.IjkVideoView;
import cn.smg.luo.smtech_video.widget.media.SurfaceRenderView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author jl_luo
 * @name: cn.smg.luo.smtech_video
 * @description:
 * @date 2016/3/8 10:57
 */

public class MediaPlayActivity extends AppCompatActivity  {//implements SurfaceHolder.Callback
    private static final String TAG = MediaPlayActivity.class.getSimpleName();

    @Bind(R.id.surface_view)
    SurfaceRenderView surfaceView;
    IjkMediaPlayer mediaPlayer;
    private String currentVideoPath;
    private boolean mBackPressed;
    private SurfaceHolder surfaceHolder;
    private IjkVideoView mVideoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        ButterKnife.bind(this);
        currentVideoPath = getIntent().getStringExtra("videopath");
//        surfaceHolder=surfaceView.getHolder();//SurfaceHolder是SurfaceView的控制接口
//        surfaceHolder.addCallback(this);//因为这个类实现了SurfaceHolder.Callback接口，所以回调参数直接this
//        surfaceHolder.setKeepScreenOn(true);

        //第二中方法
        surfaceView.setAspectRatio(0);
        surfaceView.addRenderCallback(new IRenderView.IRenderCallback() {
            @Override
            public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height) {
                mVideoView = new IjkVideoView(getApplicationContext());
                mVideoView.setVideoPath(currentVideoPath);
                mVideoView.start();
            }

            @Override
            public void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder) {

            }
        });

    }

//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        // init player
//        IjkMediaPlayer.loadLibrariesOnce(null);
//        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
//        mediaPlayer = new IjkMediaPlayer();
//        try {
//            mediaPlayer.setDataSource(currentVideoPath);
//            mediaPlayer.setDisplay(holder);
//            mediaPlayer.prepareAsync();
//            mediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        if(mediaPlayer!=null){
//            mediaPlayer.stop();
//        }
//    }

    @Override
    public void onBackPressed() {
        mBackPressed = true;
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (mBackPressed || !mediaPlayer.isPlayable()) {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//        }
//        IjkMediaPlayer.native_profileEnd();
        if (mBackPressed || !mVideoView.isBackgroundPlayEnabled()) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        } else {
            mVideoView.enterBackground();
        }
        IjkMediaPlayer.native_profileEnd();
    }

}
