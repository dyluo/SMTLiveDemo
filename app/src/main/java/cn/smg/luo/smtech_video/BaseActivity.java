package cn.smg.luo.smtech_video;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.smg.luo.smtech_video.common.WindowUtils;

/**
 * @author jl_luo
 * @name: cn.smg.luo.smtech_video
 * @description:
 * @date 2016/3/9 10:14
 */

public class BaseActivity extends AppCompatActivity {
    @Nullable
    @Bind(R.id.action_bar_title)TextView actionBarTitle;
    @Nullable
    @Bind(R.id.iv_user)ImageView ivUser;

    //播放的header
    @Nullable
    @Bind(R.id.iv_arrow_left)ImageButton ivBack;
    @Nullable
    @Bind(R.id.tv_share) ImageButton tvShare;

    protected Context mContext;
    //是否横屏了
    protected boolean isLandscape;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
    }

    /**
     * sdk_int大于4.4(kitkat),则通过代码的形式设置status bar为透明
     * 某些手机会不起效，所以采用代码的形式进行设置
     */
    protected void initWindow(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
//            Translucent status bar
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
            setStatusBarColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        }

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        initHeader();
    }

    protected void initHeader() {
    }

    /**
     * 隐藏状态栏
     * @param hide
     */
    protected void hideSystemBar(boolean hide){
//        if(!isLandscape){
//            return;
//        }
        if(hide){
//            WindowUtils.hideSystemUI(getWindow().getDecorView());
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }else{
//            WindowUtils.showSystemUI(getWindow().getDecorView());
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }


    /**
     * 设置状态栏颜色
     * 也就是所谓沉浸式状态栏
     */
    public void setStatusBarColor(int color) {
        /**
         * Android4.4以上可用
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintResource(color);
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
