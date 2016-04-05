package cn.smg.luo.smtech_video.activity;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import cn.smg.luo.smtech_video.R;

/**
 * @author jl_luo
 * @name: cn.smg.luo.smtech_video.activity
 * @description:
 * @date 2016/3/25 17:40
 */

public class ToolbarAlphaBehavior extends CoordinatorLayout.Behavior<Toolbar> {
    private static final String TAG = "ToolbarAlphaBehavior";
    private int offset = 0;
    private int startOffset = 0;
    private int endOffset = 0;
    private Context context;

    public ToolbarAlphaBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, Toolbar child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }


    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, Toolbar toolbar, View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        startOffset = 0;
        endOffset = context.getResources().getDimensionPixelOffset(R.dimen.header_height) - toolbar.getHeight();
        offset += dyConsumed;
        Log.e("Tt",offset+","+startOffset+",,"+endOffset+">"+dyConsumed);
        if (offset <= startOffset) {  //alpha为0
            toolbar.setTitle("测试");
//            toolbar.findViewById(R.id.llttw).setVisibility(View.GONE);
            toolbar.getBackground().setAlpha(0);

        } else if (offset > startOffset && offset < endOffset) { //alpha为0到255

            float precent = (float) (offset - startOffset) / endOffset;
            int alpha = Math.round(precent * 255);
            toolbar.getBackground().setAlpha(alpha);

        } else if (offset >= endOffset) {  //alpha为255

            toolbar.getBackground().setAlpha(255);
            toolbar.setTitle("");
//            toolbar.findViewById(R.id.llttw).setVisibility(View.VISIBLE);

        }
    }

}
