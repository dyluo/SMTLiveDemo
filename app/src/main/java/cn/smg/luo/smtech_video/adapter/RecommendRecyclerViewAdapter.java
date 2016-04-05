package cn.smg.luo.smtech_video.adapter;

import android.content.Context;

import cn.smg.luo.smtech_video.model.Program;

/**
 * @author jl_luo
 * @name: cn.smg.luo.smtech_video.adapter
 * @description:
 * @date 2016/4/1 15:53
 */

public class RecommendRecyclerViewAdapter extends BGARecyclerViewAdapter<Program>{

    public RecommendRecyclerViewAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    protected void fillData(BGAViewHolderHelper viewHolderHelper, int position, Program model) {

    }
}
