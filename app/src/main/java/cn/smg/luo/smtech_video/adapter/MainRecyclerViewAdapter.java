package cn.smg.luo.smtech_video.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.smg.luo.smtech_video.R;

/**
 * Created by nian_fan on 2016/4/8.
 */
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder>{

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_ITEM = 0;

    Context context;

    int[] mDatas = {
            R.mipmap.t01, R.mipmap.t01, R.mipmap.t01, R.mipmap.t01
    };

    public MainRecyclerViewAdapter(Context context){
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.main_recyclerview_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        holder.iv.setBackgroundResource(mDatas[position]);
        //holder.tv.setText("");
    }

    @Override
    public int getItemCount()
    {
        return mDatas.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView tv;
        ImageView iv;

        public MyViewHolder(View view)
        {
            super(view);
            tv = (TextView) view.findViewById(R.id.rv_recyclerview_text);
            iv = (ImageView) view.findViewById(R.id.rv_recyclerview_img);
        }
    }

}
