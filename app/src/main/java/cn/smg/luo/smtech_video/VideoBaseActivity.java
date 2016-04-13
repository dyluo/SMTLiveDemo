package cn.smg.luo.smtech_video;



import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.smg.luo.smtech_video.common.DensityUtils;
import cn.smg.luo.smtech_video.common.WindowUtils;
import cn.smg.luo.smtech_video.view.CommentDialog;
import cn.smg.luo.smtech_video.widget.media.IjkVideoView;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

;

/**
 * @author jl_luo
 * @name: cn.smg.luo.smtech_video
 * @description:
 * @date 2016/3/21 17:27
 */

public class VideoBaseActivity extends BaseActivity implements Handler.Callback{
    private static final String TAG = VideoBaseActivity.class.getSimpleName();

    protected static final int MESSAGE_HIDE_CENTER_BOX = 4;
    protected static final int MESSAGE_HIDE_LOCK_BOX = 3;
    protected static final int MESSAGE_FADE_OUT = 1;
    protected static final int TIME_HIDE_CENTER_BOX = 500;
    protected static final int TIME_FADE_OUT = 10000;
    String currentPath;
    String[] mVideoPaths;
    //top bar分割线
    @Bind(R.id.iv_div)View ivDiv;
    @Bind(R.id.iv_div2)View ivDiv2;
    //全屏按钮
    @Bind(R.id.full) ImageButton btnFull;
    //顶部标题
    @Bind(R.id.title)TextView title;
    //全屏 底部控制条信息
    @Bind(R.id.rl_video_bottom)RelativeLayout rlVideoBottom;
    //竖屏 视角控制按钮
    @Bind(R.id.rg_angle)RadioGroup rgAngle;
    //竖屏播放按钮
    @Bind(R.id.float_play)ImageButton mFloatPlay;

    @Bind(R.id.tv_angle) TextView tvAngle;
    @Bind(R.id.tv_ratio) TextView tvRatio;
    @Bind(R.id.tv_setting)ImageButton tvSetting;
    /**
     * 顶部bar
     */
    @Bind(R.id.toolbar) RelativeLayout rlVideoTop;
    //锁屏按钮
    @Bind(R.id.btn_lock_left)ImageButton btnLockLeft;
    @Bind(R.id.btn_lock_right)ImageButton btnLockRight;
    @Bind(R.id.tv_lock) ImageButton tvLock;
    @Bind(R.id.rl_video)RelativeLayout rlVideo;
    //播放器View
    @Bind(R.id.video_view)IjkVideoView mVideoView;
    //控制条
    @Bind(R.id.ll_video_control)RelativeLayout rlVideoControl;
    //弹幕
    @Nullable
    @Bind(R.id.sv_danmaku)
    DanmakuView mDanmakuView;
    //底部播控按钮
    @Bind(R.id.btn_play) ImageButton btnPlay;
    @Bind(R.id.tvWatchers)TextView tvWatchers;
    @Bind(R.id.tv_send_switch)ImageButton btnSendSwitch;
    @Bind(R.id.tv_send)ImageButton btnSendDamu;
    @Bind(R.id.tv_vertical_screen)ImageButton btnScreen;

    protected Handler mHandler;
    protected boolean isLock;
    private float screenWidthPixels;
    //最大声音
    private int mMaxVolume;
    private AudioManager audioManager;
    protected long startPlayTimes;
    @Bind(R.id.progressBar)
    protected ProgressBar mProgressBar;
    /**
     * 弹幕
     */
    protected DanmakuContext mDanmakuContext;
    protected BaseDanmakuParser mParser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(this);
    }

    /**
     * 默认视频页面头部bar
     */
    @Override
    protected void initHeader() {
        super.initHeader();
        showPlayMenu(false);
        initView();
    }

    private void initView() {
        screenWidthPixels = getResources().getDisplayMetrics().widthPixels;

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        final GestureDetector gestureDetector = new GestureDetector(this, new PlayerGestureListener());
        rlVideoControl.setClickable(true);
        rlVideoControl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event))
                    return true;

                // 处理手势结束
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        endGesture();
                        break;
                }

                return true;
            }
        });


    }
    protected void resetTopBarMargin(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT ){
           rlVideoTop.setPadding(DensityUtils.dp2px(mContext, 15), DensityUtils.dp2px(mContext,31)- WindowUtils.getStatusBarHeight(mContext),DensityUtils.dp2px(mContext,15),0);
        }
    }

    /**
     * 隐藏底部导航栏
     * @param hide
     */
    protected void hideNavigation(boolean hide){
//        if(!isLandscape){
//            return ;
//        }
        if(!WindowUtils.checkDeviceHasNavigationBar(mContext))
            return;
        if(hide) {
            int flags;
            int curApiVersion = android.os.Build.VERSION.SDK_INT;
            if (curApiVersion >= Build.VERSION_CODES.KITKAT) {
                // This work only for android 4.4+
                flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;

            } else {
                // touch the screen, the navigation bar will show
                flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }

            // must be executed in main thread :)
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }else{
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }


    private void endGesture() {
        Log.e(TAG,"~~~~~~endGesture~~~~~~~");
        volume = -1;
        brightness = -1f;
        mHandler.removeMessages(MESSAGE_HIDE_CENTER_BOX);
        mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_CENTER_BOX, TIME_HIDE_CENTER_BOX);
    }

    /**
     * 横竖屏切换UI控制
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE) {//横屏
            if(!isLandscape) {
                ViewCompat.animate(rlVideoTop).alpha(1).translationY(0).setDuration(100).start();
                isLandscape = true;
                showPlayMenu(true);
                mHandler.sendEmptyMessageDelayed(MESSAGE_FADE_OUT, TIME_FADE_OUT);
            }
        }else{
            isLandscape = false;
            showPlayMenu(false);
            hideNavigation(false);
            hideSystemBar(false);

            btnLockLeft.setVisibility(View.GONE);
            btnLockRight.setVisibility(View.GONE);
        }
        preparedDanMu();
    }

    /**
     * 显示横屏播放页面
     * @param show
     */
    protected void showPlayMenu(boolean show){
        if(show){//横屏时候
            //顶部
            btnFull.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            tvAngle.setVisibility(View.VISIBLE);
            tvRatio.setVisibility(View.VISIBLE);
            tvSetting.setVisibility(View.VISIBLE);
            ivDiv.setVisibility(View.VISIBLE);
            ivDiv2.setVisibility(View.VISIBLE);
            //底部
            rgAngle.setVisibility(View.GONE);
            rlVideoBottom.setVisibility(View.VISIBLE);
//            btnPlay.setVisibility(View.VISIBLE);
//            tvWatchers.setVisibility(View.VISIBLE);
//            btnSendSwitch.setVisibility(View.VISIBLE);
//            btnSendDamu.setVisibility(View.VISIBLE);
        }else{
            //顶部
            btnFull.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);
            tvAngle.setVisibility(View.GONE);
            tvSetting.setVisibility(View.GONE);
            tvRatio.setVisibility(View.GONE);
            ivDiv.setVisibility(View.GONE);
            ivDiv2.setVisibility(View.GONE);

            rgAngle.setVisibility(View.VISIBLE);
            rlVideoBottom.setVisibility(View.GONE);
//            btnPlay.setVisibility(View.GONE);
//            tvWatchers.setVisibility(View.GONE);
//            btnSendSwitch.setVisibility(View.GONE);
//            btnSendDamu.setVisibility(View.GONE);
        }
    }

//

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case MESSAGE_FADE_OUT:
                showVideoMenu(false);
                break;
            case MESSAGE_HIDE_CENTER_BOX:
                llBrightnessBox.setVisibility(View.GONE);
                llVolumeBox.setVisibility(View.GONE);
                break;
            case MESSAGE_HIDE_LOCK_BOX:
                btnLockLeft.setVisibility(View.GONE);
                btnLockRight.setVisibility(View.GONE);
                break;
        }
        return false;
    }

    /**
     * 全屏的时候发送弹幕事件
     * 出现弹幕对话框和对应点击事件
     */
    @OnClick(R.id.tv_send)
    void clickSend(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        CommentDialog commentDialog =  CommentDialog.newInstance(2);
        commentDialog.show(ft, "dialog");
    }

    /**
     * 点击锁屏按钮
     */
    @OnClick({R.id.tv_lock,R.id.btn_lock_right,R.id.btn_lock_left})
    void clickLockScreen(){
//        Toast.makeText(getApplicationContext(),"~~~~~lockScreen~~~~~",Toast.LENGTH_SHORT).show();
        isLock = !isLock;
        if(isLock){
            showVideoMenu(false);
            tvLock.setBackgroundResource(R.mipmap.tv_locked);
            btnLockLeft.setBackgroundResource(R.mipmap.tv_locked);
            btnLockRight.setBackgroundResource(R.mipmap.tv_locked);
        }else{
            showVideoMenu(true);
            tvLock.setBackgroundResource(R.mipmap.tv_lock);
            btnLockLeft.setBackgroundResource(R.mipmap.tv_lock);
            btnLockRight.setBackgroundResource(R.mipmap.tv_lock);
        }
        showLockScreen(isLock);
    }

    private void showLockScreen(boolean isLock){

    }

    @OnClick({R.id.tv_vertical_screen,R.id.full})
    void ClickChangeVideoScreen(){
        changeScreen();
    }

    @OnClick({R.id.btn_play,R.id.float_play})
    void clickPlay(){
        rlVideoControl.setBackgroundResource(0);
        if(mVideoView!= null && mVideoView.canPause() && mVideoView.isPlaying()){
            mVideoView.pause();
            mFloatPlay.setImageResource(R.mipmap.btn_play_float);
            btnPlay.setBackgroundResource(R.mipmap.btn_full_play);
        }else if(mVideoView!=null && !mVideoView.isPlaying()){
            mVideoView.start();
            mFloatPlay.setImageResource(R.mipmap.btn_pause_float);
            btnPlay.setBackgroundResource(R.mipmap.btn_full_pause);
        }
    }
    @OnClick(R.id.tv_send_switch)
    void clickDanmuSwitch(){
//        Log.e(TAG, "~~~~" + mDanmakuView.isShown() + ">>" + mDanmakuView.isPaused());
        if (mDanmakuView != null && mDanmakuView.isPrepared() && !mDanmakuView.isPaused()) {
            mDanmakuView.hideAndPauseDrawTask();
            btnSendSwitch.setBackgroundResource(R.mipmap.btn_danmu_select);
        }else if(mDanmakuView!= null && mDanmakuView.isPaused()){
            mDanmakuView.showAndResumeDrawTask(SystemClock.currentThreadTimeMillis() - startPlayTimes);
            btnSendSwitch.setBackgroundResource(R.mipmap.btn_danmu);
//            mDanmakuView.show();
//            mDanmakuView.resume();
        }
    }

    /**
     * 横竖屏切换的时候，初始化弹幕，或者
     */
    void preparedDanMu(){
        if (mDanmakuView != null && !mDanmakuView.isPrepared() && mVideoView.isPlaying()){
            mDanmakuView.prepare(mParser, mDanmakuContext);
            return;
        }
        if(isLandscape && mDanmakuView!= null && mDanmakuView.isPrepared()){
            mDanmakuView.showAndResumeDrawTask(SystemClock.currentThreadTimeMillis() - startPlayTimes);

        }else if(!isLandscape && mDanmakuView!= null && mDanmakuView.isPrepared()){
            mDanmakuView.hideAndPauseDrawTask();
        }
    }
    /**
     * 是否显示顶部和底部菜单
     * @param show
     */
    protected void showVideoMenu(boolean show){
//        if(!isLandscape){
//            return;
//        }
        mHandler.removeMessages(MESSAGE_FADE_OUT);
        if(show){//显示播控信息
            ViewCompat.animate(rlVideoTop).alpha(1).translationY(0).setDuration(500).start();
            showVideoBottom(show);
            mHandler.sendEmptyMessageDelayed(MESSAGE_FADE_OUT, TIME_FADE_OUT);
            hideSystemBar(false);
            hideNavigation(false);
            //显示锁定按钮
            if(btnLockLeft.getVisibility() == View.VISIBLE) {
                btnLockLeft.setVisibility(View.GONE);
                btnLockRight.setVisibility(View.GONE);
            }
        }else{
            ViewCompat.animate(rlVideoTop).alpha(0).translationY(-rlVideoTop.getHeight()).setDuration(500).start();
            showVideoBottom(show);
            hideSystemBar(true);
            if(isLandscape) {
                hideNavigation(true);
            }
        }
    }

    /**
     * 显示或者隐藏底部播控条，有竖屏和横屏区分
     * @param show
     */
    protected void showVideoBottom(boolean show){
        if(show){
            if(isLandscape){
                ViewCompat.animate(rlVideoBottom).alpha(1).setDuration(500).start();
            }else{
                ViewCompat.animate(rgAngle).alpha(1).setDuration(500).start();
            }
        }else{
            if(isLandscape){
                    ViewCompat.animate(rlVideoBottom).alpha(0).setDuration(500).start();
            }else{
                ViewCompat.animate(rgAngle).alpha(0).setDuration(500).start();
            }
        }
    }

    /**
     * 显示或者隐藏锁定按钮
     */
    protected void viLockView(){
        if(!isLandscape){
            return;
        }
        mHandler.removeMessages(MESSAGE_HIDE_LOCK_BOX);
        if(btnLockLeft.getVisibility() == View.GONE){
            btnLockLeft.setVisibility(View.VISIBLE);
            btnLockRight.setVisibility(View.VISIBLE);
            mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_LOCK_BOX, TIME_FADE_OUT);
        }else{
            btnLockLeft.setVisibility(View.GONE);
            btnLockRight.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && isLock){
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_MENU && isLock){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 在视频上进行手势操作的时候
     */
    private class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean firstTouch;
        private boolean volumeControl;
        @Override
        public boolean onDown(MotionEvent e) {
            Log.e(TAG,"我点击了额 ondown");
            firstTouch = true;
            return super.onDown(e);
        }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.e(TAG,"onSingleTapConfirmed~~~~~~~~~~");
            return super.onSingleTapConfirmed(e);
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {Log.e(TAG,"我点击了结束onSingleTapUp~~~~~~~~~~");
//            if(!isLandscape){//竖屏的时候点击
//                Log.e(TAG,"我点击了~~~~~~~~~~");
//                changeScreen();
//                return true;
//            }
            if(isLock ){
                viLockView();
                return true;
            }
//            if(isLandscape){
                if(rlVideoTop.getAlpha()==1f){
                    showVideoMenu(false);
                }else if(rlVideoTop.getAlpha() == 0){
                    showVideoMenu(true);
                }
//                viLockView();
//            }
            return super.onSingleTapUp(e);
        }
        //左滑动，右滑动控制声音和亮度
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(!isLandscape){
                return true;
            }
            if(isLock){
                return true;
            }
            float mOldX = e1.getX(), mOldY = e1.getY();
            float deltaY = mOldY - e2.getY();
//            float deltaX = mOldX - e2.getX();
            if (firstTouch) {
                volumeControl= mOldX > screenWidthPixels * 0.5f;
                firstTouch = false;
            }
            float percent = deltaY / mVideoView.getHeight() ;
            if (volumeControl) {
                onVolumeSlide(percent);
            } else {
                onBrightnessSlide(percent);
            }
            return false;
        }

    }
    private float brightness=-1;
    private int volume=-1;
    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (volume == -1) {
            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (volume < 0)
                volume = 0;
        }
        showVideoMenu(false);

        int index = (int) (percent * mMaxVolume) + volume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        // 变更进度条
        int i = (int) (index * 1.0 / mMaxVolume * 100);
        String s = i + "%";
        if (i == 0) {
            s = "off";
        }
        // 显示
        showVolume(i,s);
    }
    @Bind(R.id.app_video_volume_box)LinearLayout llVolumeBox;
    @Bind(R.id.app_video_volume_icon) ImageView icVolume;
    @Bind(R.id.app_video_volume)TextView tvVolume;
    protected void showVolume(int i,String s){
        llBrightnessBox.setVisibility(View.GONE);
        llVolumeBox.setVisibility(View.VISIBLE);
        if(i==0){
            icVolume.setImageResource(R.mipmap.ic_volume_off_white_36dp);
        }else{
            icVolume.setImageResource(R.mipmap.ic_volume_up_white_36dp);
        }
        tvVolume.setText(s);
    }

    @Bind(R.id.app_video_brightness_box)LinearLayout llBrightnessBox;
//    @Bind(R.id.app_video_brightness_icon) ImageView icBrightness;
    @Bind(R.id.app_video_brightness)TextView tvBrightness;
    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        llVolumeBox.setVisibility(View.GONE);
        if (brightness < 0) {
            brightness = getWindow().getAttributes().screenBrightness;
            if (brightness <= 0.00f){
                brightness = 0.50f;
            }else if (brightness < 0.01f){
                brightness = 0.01f;
            }
        }
       llBrightnessBox.setVisibility(View.VISIBLE);
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = brightness + percent;
        if (lpa.screenBrightness > 1.0f){
            lpa.screenBrightness = 1.0f;
        }else if (lpa.screenBrightness < 0.01f){
            lpa.screenBrightness = 0.01f;
        }
        tvBrightness.setText(((int) (lpa.screenBrightness * 100)) + "%");
        getWindow().setAttributes(lpa);

    }

    protected void changeScreen(){
        /**
         * 横屏切换为竖屏
         */
        if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

    }


}
