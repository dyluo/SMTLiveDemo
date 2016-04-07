package cn.smg.luo.smtech_video.widget.menus;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import cn.smg.luo.smtech_video.R;
import cn.smg.luo.smtech_video.common.DensityUtils;

/**
 * 视角和画质下拉菜单
 * @author jl_luo
 * @name: cn.smg.luo.smtech_video.widget.menus
 * @description:
 * @date 2016/3/24 17:18
 */

public class AnglePopView extends PopupWindow implements RadioGroup.OnCheckedChangeListener{
    Context context;
    private View contentView;
    ColorDrawable tempDrawable;
    private LinearLayout iconTag;

    public AnglePopView(Context context){
        this.context = context;
        Drawable d = ContextCompat.getDrawable(context, R.mipmap.ic_menu_arrow);
        tempDrawable = new ColorDrawable(0x00000000);
        tempDrawable.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
        init();
    }

    private void init(){
        LayoutInflater inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(R.layout.pop_angle,null);
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density1 = dm.density;
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        // 设置SelectPicPopupWindow的View
        this.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(DensityUtils.dp2px(context,137));
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();

        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimationFade);
        initView();
    }

    private void initView() {
        RadioGroup radioGroup = (RadioGroup) contentView.findViewById(R.id.rg_angle);
        radioGroup.setOnCheckedChangeListener(this);
        iconTag = (LinearLayout)contentView.findViewById(R.id.ll_icon_tag);
    }



    /**
     * 点击事件，切换图片位置，修改viedo播放视角
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int id= group.getCheckedRadioButtonId();
        switch (id) {
            case R.id.radio1:
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) iconTag.getLayoutParams();
                layoutParams.setMargins(0,0,0,0);
                iconTag.setLayoutParams(layoutParams);
                videoAngleChangeListener.change(0);
                break;
            case R.id.radio2:
                layoutParams = (FrameLayout.LayoutParams) iconTag.getLayoutParams();
                layoutParams.setMargins(0,DensityUtils.dp2px(context,28.5f),0,0);
                iconTag.setLayoutParams(layoutParams);
                videoAngleChangeListener.change(1);
                break;
            case R.id.radio3:
                layoutParams = (FrameLayout.LayoutParams) iconTag.getLayoutParams();
                layoutParams.setMargins(0,DensityUtils.dp2px(context,28*2+1),0,0);
                iconTag.setLayoutParams(layoutParams);
                videoAngleChangeListener.change(2);
                break;
            case R.id.radio4:
                layoutParams = (FrameLayout.LayoutParams) iconTag.getLayoutParams();
                layoutParams.setMargins(0,DensityUtils.dp2px(context,28*3+1.5f),0,0);
                iconTag.setLayoutParams(layoutParams);
                videoAngleChangeListener.change(3);
                break;
            default:
                break;
        }
    }

    public interface VideoAngleChangeListener{
        public void change(int num);
    }

    private VideoAngleChangeListener videoAngleChangeListener;
    public void setVideoAngleChangeListener(VideoAngleChangeListener videoAngleChangeListener){
        this.videoAngleChangeListener = videoAngleChangeListener;
    }

}
