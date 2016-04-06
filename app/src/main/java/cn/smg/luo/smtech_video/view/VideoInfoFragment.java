package cn.smg.luo.smtech_video.view;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import cn.smg.luo.smtech_video.R;
import cn.smg.luo.smtech_video.VideoActivity;
import cn.smg.luo.smtech_video.adapter.RecommendRecyclerViewAdapter;
import cn.smg.luo.smtech_video.adapter.SpacesItemDecoration;
import cn.smg.luo.smtech_video.common.GlideCircleTransform;
import cn.smg.luo.smtech_video.common.TimeUtils;
import cn.smg.luo.smtech_video.model.Program;
import master.flame.danmaku.danmaku.model.BaseDanmaku;

/**
 * @author jl_luo
 * @name: cn.smg.luo.smtech_video.view
 * @description:
 * @date 2016/4/1 10:48
 */

public class VideoInfoFragment extends BaseFragment{

    private static final int COMMENTSIZE = 4;
    //评论
    @Bind(R.id.llComments)LinearLayout llComments;
    @Bind(R.id.recycle_recommend)RecyclerView mRecommends;
    private GridLayoutManager mLayoutMgr;
    //评论收缩按钮
    @Bind(R.id.ic_comment_toggle)ImageView icCommentToggle;

    public static VideoInfoFragment newInstance(int num) {
        VideoInfoFragment fragment = new VideoInfoFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_details);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onUserVisible();
    }

    @Override
    protected void onUserVisible() {
//        Log.e(TAG,"~~~~~~~onUserVisible~~~~~~");
        llComments = build(llComments);
        llComments = addCommentMore(llComments);
        initRecommend();
    }

    /**
     * 添加四条评论
     * @param container
     * @return
     */
    private LinearLayout build(LinearLayout container){
        VideoActivity parent = (VideoActivity) getActivity();
        ArrayList<BaseDanmaku> list = parent.getDanmuList();
        int size = list.size();
        for(int i=0;i<size;i++){
            View itemCommentView = LayoutInflater.from(context).inflate(R.layout.item_commnt,null);
            ImageView head = (ImageView) itemCommentView.findViewById(R.id.ic_head);
            Glide.with(context).load(R.mipmap.ic12)
                    .centerCrop()
                    .crossFade()
                    .transform(new GlideCircleTransform(context))
                    .into(head);
            TextView userName = (TextView)itemCommentView.findViewById(R.id.txt_username);
            userName.setText(userName.getText() + "" + i);
            ((TextView)itemCommentView.findViewById(R.id.txt_content)).setText(list.get(i).text);
            ((TextView)itemCommentView.findViewById(R.id.txt_time)).setText(TimeUtils.getTime(list.get(i).time,TimeUtils.DEFAULT_DATE_FORMAT));
            container.addView(itemCommentView);
        }

        return container;
    }

    /**
     * 添加查看更多评论
     * @param container
     * @return
     */
    private LinearLayout addCommentMore(LinearLayout container){
        View itemCommentView = LayoutInflater.from(context).inflate(R.layout.item_comment_more,null);
        itemCommentView.setOnClickListener(new View.OnClickListener(){
                                               @Override
                                               public void onClick(View v) {
                                                   Toast.makeText(context,"more...",Toast.LENGTH_SHORT).show();
                                               }
                                           }
        );
        container.addView(itemCommentView);
        return container;
    }

    /**
     * 添加精彩推荐
     */
    private void initRecommend(){
        mLayoutMgr = new GridLayoutManager(getActivity(), 2);
//        mRecommends.setHasFixedSize(true);
        mRecommends.setLayoutManager(mLayoutMgr);
        mRecommends.setItemAnimator(new DefaultItemAnimator());
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing4);
//        mRecommends.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        RecommendRecyclerViewAdapter rcAdapter = new RecommendRecyclerViewAdapter(context,R.layout.card_video);
        mRecommends.setAdapter(rcAdapter);
        ArrayList<Program> list = new ArrayList<>();
        for(int i=0;i<COMMENTSIZE;i++){
            Program p = new Program();
            p.videoName = "isd习近平在华盛顿会见奥巴马"+i;
            list.add(p);
        }
        rcAdapter.addMoreDatas(list);

    }

    /**
     * 评论显示和不显示操作
     */
    @OnClick(R.id.ic_comment_toggle)
    void commentToggleClick(){
        llComments.setVisibility((llComments.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
        icCommentToggle.setImageResource(llComments.getVisibility() == View.VISIBLE ? R.mipmap.ic_togg_close: R.mipmap.ic_togg_open);

    }
}
