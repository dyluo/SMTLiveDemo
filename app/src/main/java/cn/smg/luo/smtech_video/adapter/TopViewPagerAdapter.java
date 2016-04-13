package cn.smg.luo.smtech_video.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;


/**
 * @author jl_luo
 * @name: cn.smg.luo.smtech_video.adapter
 * @description:
 * @date 2015/12/16 16:01
 */

public class TopViewPagerAdapter extends PagerAdapter {
    private ArrayList<View> imgsList;

    Context context;

    public TopViewPagerAdapter(ArrayList<View> imgsList){

        this.imgsList = imgsList;
    }

    @Override
    public void destroyItem(View arg0, int position, Object arg2) {
        ((ViewPager) arg0).removeView(imgsList.get(position));
    }

    @Override
    public int getCount() {
        return imgsList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(imgsList.get(position), 0);
        return imgsList.get(position);
    }
}
