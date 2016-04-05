package cn.smg.luo.smtech_video.widget.menus;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import cn.smg.luo.smtech_video.R;
import cn.smg.luo.smtech_video.common.DensityUtils;

/**
 * 画质下拉菜单
 * @author jl_luo
 * @name: cn.smg.luo.smtech_video.widget.menus
 * @description:
 * @date 2016/3/24 17:18
 */

public class RatioPopView extends PopupWindow{
    Context context;
    private View contentView;
    public RatioPopView(Context context){
        this.context = context;
        init();
    }

    private void init(){
        LayoutInflater inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(R.layout.pop_ratio,null);
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density1 = dm.density;
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        // 设置SelectPicPopupWindow的View
        this.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(DensityUtils.dp2px(context,100));
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

    }

}
