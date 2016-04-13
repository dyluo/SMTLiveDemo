package cn.smg.luo.smtech_video;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smg.luo.smtech_video.adapter.MainRecyclerViewAdapter;
import cn.smg.luo.smtech_video.adapter.TopViewPagerAdapter;
import cn.smg.luo.smtech_video.common.DensityUtils;
import cn.smg.luo.smtech_video.common.WindowUtils;
import cn.smg.luo.smtech_video.model.VideoPath;
import cn.smg.luo.smtech_video.widget.CircleIndicator;

public class MainActivity extends BaseActivity  {

    private static final String TAG = MainActivity.class.getName();
    protected Context context;
    private TopViewPagerAdapter mTopViewPageAdapter;
    public ArrayList<View> imagesList = new ArrayList<>();
    private MainRecyclerViewAdapter mAdapter;
    public static final String paths[] = new String[]{VideoPath.testRtmpPath1,VideoPath.testRtmpPath2,VideoPath.testRtmpPath3,VideoPath.testRtmpPath4,VideoPath.tt1_video1};
    @Nullable
    @Bind(R.id.banner_header)FrameLayout mBannerView;

    @Nullable
    @Bind(R.id.myviewpager)ViewPager mTopPagerBanner;

    @Nullable
    @Bind(R.id.pager_indicator)CircleIndicator mPagerCircleIndicator;

    @Nullable
    @Bind((R.id.rv_recyclerview_data))RecyclerView mRecycleView;
    @Bind(R.id.loginIcon)ImageView mLoginIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        ButterKnife.bind(this);
        resetTopBarMargin();
        initBanner();
        initRecyclerView();
        if(imagesList!=null)
            showBannerViews(imagesList);
    }
//    @Bind(R.id.toolbar) Toolbar mToolbar;
    protected void resetTopBarMargin(){
//        setSupportActionBar(mToolbar);
//        mToolbar.setPadding(0, WindowUtils.getStatusBarHeight(mContext),0,0);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT ){
//            mToolbar.setPadding(0, DensityUtils.dp2px(mContext,31)-WindowUtils.getStatusBarHeight(mContext),0,0);
//              mLoginIcon.setPadding(0, DensityUtils.dp2px(mContext,31)- WindowUtils.getStatusBarHeight(mContext),0,0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mLoginIcon.getLayoutParams();
            layoutParams.topMargin = DensityUtils.dp2px(mContext,31)- WindowUtils.getStatusBarHeight(mContext);
        }

    }
    public void initRecyclerView(){
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new MainRecyclerViewAdapter(context);

        mRecycleView.setAdapter(mAdapter);
    }

    int[] bannerImages = {
            R.mipmap.bb, R.mipmap.bb, R.mipmap.bb, R.mipmap.bb, R.mipmap.bb
    };

    public void initBanner(){
        for(int imageId:bannerImages){
            ImageView img = new ImageView(this);
            img.setImageDrawable(getResources().getDrawable(imageId));
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imagesList.add(img);
        }
    }

    /**
     * 填充banner数据,设置banner点击事件
     * 设置banner到mRefreshLayout中
     * @param datas
     */

    public static int itemPos = 0;
    public int getItemPos(){
        return itemPos;
    }

    public void setItemPos(int itemPos){
        this.itemPos = itemPos;
    }

    private void showBannerViews(final ArrayList<View> datas){
        if(datas!=null) {
            mTopViewPageAdapter = new TopViewPagerAdapter(datas);
            mTopPagerBanner.setAdapter(mTopViewPageAdapter);
            mTopViewPageAdapter.notifyDataSetChanged();
            mPagerCircleIndicator.setViewPager(mTopPagerBanner);

            mTopPagerBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    mTopPagerBanner.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                                case MotionEvent.ACTION_MOVE:
                                    break;
                            }
                            return false;
                        }
                    });

                }

                @Override
                public void onPageSelected(int position) {
                    setItemPos(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    @OnClick({R.id.btn_play_main,R.id.loginIcon})
    void clickPlay(View view){
        switch (view.getId()){
            case R.id.btn_play_main:
//                Toast.makeText(mContext,"position:"+getItemPos(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                intent.putExtra("videopath", paths[getItemPos()]);
                startActivity(intent);
                break;
            case R.id.loginIcon:
                Toast.makeText(mContext,"position:"+getItemPos(),Toast.LENGTH_SHORT).show();
                break;

        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//            startActivity(intent);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


}
