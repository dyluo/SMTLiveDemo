package cn.smg.luo.smtech_video;


import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.smg.luo.smtech_video.common.DensityUtils;
import cn.smg.luo.smtech_video.model.VideoPath;
import cn.smg.luo.smtech_video.view.VideoInfoFragment;
import cn.smg.luo.smtech_video.widget.media.IjkVideoView;
import cn.smg.luo.smtech_video.widget.menus.AnglePopView;
import cn.smg.luo.smtech_video.widget.menus.RatioPopView;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.Danmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author jl_luo
 * @name: cn.smg.luo.smtech_video
 * @description: 播放详情页面
 * @date 2016/3/7 15:09
 */

public class VideoActivity extends VideoBaseActivity{
    private static final String TAG = VideoActivity.class.getSimpleName();

    //详情页正文信息
    @Bind(R.id.ll_info)LinearLayout llInfo;
    private boolean mBackPressed;
    //底部点评
    @Bind(R.id.rl_comment_send)FrameLayout rlCommentSend;
    @Bind(R.id.rl_comment_send_body)RelativeLayout ivCommentBackGround;
    @Bind(R.id.et_comment) EditText mEditCommentText;
    //模糊白色
    @Bind(R.id.bg_white) ImageView mBgWhite;
    //video的默认高度
    private static final int mVideoHeight = 254;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        resetTopBarMargin();
        currentPath = getIntent().getStringExtra("videopath");
        initBaseView();
        initDamu();
        if (initIjkVideo()) return;
        initFragmentInfo(savedInstanceState);
        applyBlur(mBgWhite);
    }
    private  void initBaseView(){
        title.setText("SHAV");
    }
    /**
     * 初始化VideoView
     * @return true exit
     */
    private boolean initIjkVideo() {
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Throwable e) {
            Log.e("GiraffePlayer", "loadLibraries error", e);
        }
        mVideoView.setRender(IjkVideoView.RENDER_SURFACE_VIEW);
        if (currentPath != null) {
            mVideoView.setVideoPath(currentPath);
            mVideoPaths = new String[4];
            mVideoPaths[0] = currentPath;
            mVideoPaths[1] = VideoPath.tt1_video1;
            mVideoPaths[2] = VideoPath.tt1_video2;
            mVideoPaths[3] = VideoPath.tt1_video3;
        }
        else {
            Log.e(TAG, "Null Data Source\n");
            finish();
            return true;
        }
        mVideoView.toggleAspectRatio(0);
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                Log.e(TAG,"~~~~~~~video prepared~~~~~~~~~");
                mProgressBar.setVisibility(View.GONE);
                mHandler.sendEmptyMessageDelayed(MESSAGE_FADE_OUT, TIME_FADE_OUT);
                startPlayTimes = SystemClock.currentThreadTimeMillis();
                if (mDanmakuView != null) {
                    ViewGroup.LayoutParams params = mDanmakuView.getLayoutParams();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = mVideoView.getHeight();
                    mDanmakuView.setLayoutParams(params);
                }
                if (isLandscape) {
                    if(!mDanmakuView.isPrepared()){
                        mDanmakuView.prepare(mParser, mDanmakuContext);
                    }
                    return;
                }
                showPlayMenu(false);
                //开始弹幕
//                mDanmakuView.prepare(mParser, mDanmakuContext);
            }
        });
        return false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG,"~~~~~~onRestart~~~~~~~~~~~~");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e(TAG, "~~~~~~~~onResume~~~~~~~~~");
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
       Log.e(TAG, "~~~~~~~~~onresume~~" + mVideoView.isPlaying() + "," + mVideoView.isActivated() + "," + mVideoView.isBackgroundPlayEnabled() + ",");
    }

    /**
     * 初始化弹幕
     */
    private void initDamu(){
//        Log.e(TAG,"~~~~~~~~~~~~~~~"+mVideoView.getMeasuredHeight());
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 3); // 滚动弹幕最大显示3行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mDanmakuContext = DanmakuContext.create();
        mDanmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f).setScaleTextSize(1.0f)
//                .setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
//        .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);

        if (mDanmakuView != null) {
            mParser = createParser(this.getResources().openRawResource(R.raw.comments));
            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {
//                    Log.w(TAG, "drawingFinished(): ");
                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
                    Log.v(TAG, "danmakuShown(): text=" + danmaku.text + ",time= " + danmaku.time);
                }

                @Override
                public void prepared() {
                    Log.e(TAG,"~~~~~~~~~dan mu prepared~~~~~~~~~~~");
                    mDanmakuView.start();
                }
            });
            //弹幕点击必须
            mDanmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {
                @Override
                public void onDanmakuClick(BaseDanmaku latest) {
                    Log.e(TAG, "onDanmakuClick text:" + latest.text);
                }

                @Override
                public void onDanmakuClick(IDanmakus danmakus) {
                    Log.e(TAG, "onDanmakuClick danmakus size:" + danmakus.size());
                }
            });

            mDanmakuView.showFPS(false);
            mDanmakuView.enableDanmakuDrawingCache(true);
        }
//        getDanMuList();
    }

    /**
     * 获取弹幕数据列表
     */
    public ArrayList<BaseDanmaku> getDanmuList(){
        ArrayList<BaseDanmaku> list = new ArrayList<>();
        BaseDanmaku baseDanmaku = new Danmaku("大宝大宝天天见");
        baseDanmaku.time = 1422204613;
        list.add(baseDanmaku);
        baseDanmaku = new Danmaku("没猜到结局23333");
        baseDanmaku.time = 1422204613;
        list.add(baseDanmaku);
        baseDanmaku = new Danmaku("笑得停不下来2333333333333333333333");
        baseDanmaku.time = 1422204613;
        list.add(baseDanmaku);
        baseDanmaku = new Danmaku("刷歌词挡字母的 你药不能停");
        baseDanmaku.time = 1422204613;
        list.add(baseDanmaku);
//        if(mParser!=null){
//            mParser.setConfig(DanmakuContext.create());
//            IDanmakus iDanmakus = mParser.getDanmakus();
//            IDanmakuIterator it = iDanmakus.iterator();
//            int i= 0;
//            while(it.hasNext()){
//                if(i==4){
//                    break;
//                }
//                BaseDanmaku baseDanmaku = it.next();
//                list.add(baseDanmaku);
//                i++;
//            }
//        }
        return list;
    }

    /**
     * 弹幕列表解析
     * @param stream 弹幕xml流
     * @return BaseDanmakuParser class
     */
    private BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }
    
    
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * 添加文本弹幕
     * @param islive 是否直播
     * @param text 发送弹幕文本
     */
    public void addDanmaku(boolean islive, String text) {
        Log.e(TAG,"~~~~~~addDanmaku~~~~"+text);
        BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }

        if(TextUtils.isEmpty(text)){
            danmaku.text = "这是一条弹幕" + System.nanoTime();
        }else{
            danmaku.text = text;
        }

        danmaku.padding = 5;
        danmaku.priority = 1;  // 可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = islive;
        danmaku.time = mDanmakuView.getCurrentTime() + 1200;
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = Color.WHITE;
        // danmaku.underlineColor = Color.GREEN;
        danmaku.borderColor = Color.GREEN;
        mDanmakuView.addDanmaku(danmaku);

    }


    /**
     *
     * @param newConfig 横竖屏config
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){//横屏
            isLandscape = true;
            llInfo.setVisibility(View.GONE);
            ViewGroup.LayoutParams rlVideoLayoutParams =rlVideo.getLayoutParams();
            rlVideoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            rlVideoLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            rlVideo.setLayoutParams(rlVideoLayoutParams);

            if(mDanmakuView!=null&& mVideoView!=null && mVideoView.getHeight()!=0){
                ViewGroup.LayoutParams params = mDanmakuView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = mVideoView.getHeight();
                mDanmakuView.setLayoutParams(params);
            }
            rlCommentSend.setVisibility(View.GONE);
        }else{//竖屏
            isLandscape = false;
            llInfo.setVisibility(View.VISIBLE);
            showOrientationBar();

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            ViewGroup.LayoutParams rlVideoLayoutParams =rlVideo.getLayoutParams();
            rlVideoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            rlVideoLayoutParams.height = DensityUtils.dp2px(getApplicationContext(),mVideoHeight);
            rlVideo.setLayoutParams(rlVideoLayoutParams);

            if(mDanmakuView!=null && mVideoView!=null && mVideoView.getHeight()!=0){
                ViewGroup.LayoutParams params = mDanmakuView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height =DensityUtils.dp2px(getApplicationContext(), mVideoHeight);
                mDanmakuView.setLayoutParams(params);
            }
        }
        changeElements();
    }

    /**
     * 横竖屏切换的时候，某些元素需要隐藏和现实
     */
    private void changeElements(){
        if(isLandscape){
            rlCommentSend.setVisibility(View.GONE);
            mFloatPlay.setVisibility(View.GONE);

        }else {
            rlCommentSend.setVisibility(View.VISIBLE);
            mFloatPlay.setVisibility(View.VISIBLE);
        }
    }

    private void showOrientationBar(){
        if (ivBack != null) {
            ivBack.setVisibility(View.VISIBLE);
        }
        if (tvShare != null) {
            tvShare.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化详情页
     */
    private void initFragmentInfo(Bundle savedInstanceState){
        // 不过，如果我们要从先前的状态还原，
        // 则无需执行任何操作而应返回
        // 否则就会得到重叠的 Fragment 。
        if (savedInstanceState != null) {
            return;
        }
        // 创建一个要放入 Activity 布局中的新 Fragment
        VideoInfoFragment fragment = new VideoInfoFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_info, fragment).commit();

    }

    /**
     * 底部菜单缓存
     */
    Bitmap backBitMap ;

    /**
     * 底部菜单高斯模糊处理
     * @param backImg 对应模糊处理的图片
     */
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void applyBlur(final ImageView backImg) {
        Log.e(TAG,"~~~~~~~~applyBlur~~~~~~~~~~~~");
        if(backBitMap!=null){
            return;
        }
        ivCommentBackGround.getBackground().setAlpha(0);
//        backImg.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                Log.e(TAG,"~~~~~~~~~~~~~~~~~~sdfasdf");
//                backImg.getViewTreeObserver().removeOnPreDrawListener(this);
//                backImg.buildDrawingCache();
//                Bitmap bmp = backImg.getDrawingCache();
//
//                backBitMap = FastBlur.doBlur(getApplicationContext(), bmp, 15, true);
//                ivCommentBackGround.setBackground(new BitmapDrawable(getResources(), backBitMap));
////                bmp.recycle();
//
//                return true;
//            }
//        });

    }

    /**
     * 分享
     */
    @OnClick(R.id.tv_share)
    void share(){
        Toast.makeText(getApplicationContext(), "share", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.iv_arrow_left)
    void backPage(){
        onBackPressed();
    }

    /**
     * 视角点击显示菜单
     */
    RatioPopView ratioPopView ;
    AnglePopView anglePopView;
    @OnClick(R.id.tv_angle)
    void clickShowAngleView(){
        if(anglePopView == null) {
            anglePopView = new AnglePopView(mContext);
            anglePopView.setVideoAngleChangeListener(new AnglePopView.VideoAngleChangeListener() {
                @Override
                public void change(int num) {
                    changeVideoAngle(mVideoPaths[num]);
                    anglePopView.dismiss();
                }
            });
        }
        if(anglePopView.isShowing()){
            anglePopView.dismiss();
        }else{
            anglePopView.showAsDropDown(tvAngle);
            mHandler.removeMessages(MESSAGE_FADE_OUT);
        }
    }

    /**
     * 画质
     */
    @OnClick(R.id.tv_ratio)
    void clickShowRatioView(){
        if(ratioPopView == null) {
            ratioPopView = new RatioPopView(mContext);
            ratioPopView.setVideoAngleChangeListener(new AnglePopView.VideoAngleChangeListener() {
                @Override
                public void change(int num) {
                    ratioPopView.dismiss();
                }
            });
        }
        if(ratioPopView.isShowing()){
            ratioPopView.dismiss();
        }else{
            ratioPopView.showAsDropDown(tvRatio);
            mHandler.removeMessages(MESSAGE_FADE_OUT);
        }
    }
    /**
     * 栏目切换
     * @param view 视角radioButton
     */
    @OnClick({R.id.radio1,R.id.radio2,R.id.radio3,R.id.radio4})
    void changeVideoClick(View view){
        switch (view.getId()){
            case R.id.radio1:
                changeVideoAngle(mVideoPaths[0]);
                break;
            case R.id.radio2:
                changeVideoAngle(mVideoPaths[1]);
                break;
            case R.id.radio3:
                changeVideoAngle(mVideoPaths[2]);
                break;
            case R.id.radio4:
                changeVideoAngle(mVideoPaths[3]);
                break;
        }
    }

    /**
     * 视角切换
     * @param targetVideo 目标视频地址
     */
    private void changeVideoAngle(String targetVideo) {
        if(!currentPath.equals(targetVideo)){
            mProgressBar.setVisibility(View.VISIBLE);
            mVideoView.pause();
            mVideoView.setVideoPath(targetVideo);
            currentPath = targetVideo;
            mVideoView.start();
        }
    }

    /**
     * 发送弹幕事件
     */
    @OnClick(R.id.tv_send_danmu)
    void clickSendComment(){
        String comment = mEditCommentText.getText().toString();
        if(!TextUtils.isEmpty(comment) ){//&& mDanmakuView.isPrepared()
            Snackbar snackbar = Snackbar
                    .make(rlVideo, "发送成功", Snackbar.LENGTH_LONG);

            View v = snackbar.getView();
            v.setBackgroundColor(ContextCompat.getColor(mContext,R.color.colorFloatAction));
            snackbar.show();
        }else{
            Snackbar snackbar = Snackbar
                    .make(rlVideo, "error~", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //暂停并修改按钮图片
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            clickPlay();
        }
    }

    @Override
    public void onBackPressed() {
        if(isLandscape){
            changeScreen();
            return;
        }
        mBackPressed = true;
        super.onBackPressed();

        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
        mHandler.removeMessages(MESSAGE_FADE_OUT);
        mHandler.removeMessages(MESSAGE_HIDE_LOCK_BOX);
        mHandler.removeMessages(MESSAGE_HIDE_CENTER_BOX);
    }



    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"~~~~~~onStop~~~~~");
        if (mBackPressed || !mVideoView.isBackgroundPlayEnabled()) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        } else {
            mVideoView.enterBackground();
        }
        IjkMediaPlayer.native_profileEnd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
        if(backBitMap!=null){
            backBitMap.recycle();
            backBitMap = null;
        }
    }
}
