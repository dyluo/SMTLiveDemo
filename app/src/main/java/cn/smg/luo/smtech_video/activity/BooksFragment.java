package cn.smg.luo.smtech_video.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.smg.luo.smtech_video.R;

/**
 * @author jl_luo
 * @name: cn.smg.luo.smtech_video.activity
 * @description:
 * @date 2016/3/28 14:29
 */

public class BooksFragment extends Fragment {
    @Bind(R.id.title)
    TextView title;

    public static BooksFragment newInstance(int num) {
        BooksFragment fragment = new BooksFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         Bundle bundle = getArguments();
        title.setText("Fragment"+bundle.getInt("num"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
