package cn.smg.luo.smtech_video.adapter;

import android.view.View;

/**
 * 描述:AdapterView和RecyclerView的item中子控件长按事件监听器
 */
public interface BGAOnItemChildLongClickListener {
    boolean onItemChildLongClick(View v, int position);
}