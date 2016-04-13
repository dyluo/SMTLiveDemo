package cn.smg.luo.smtech_video.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smg.luo.smtech_video.R;
import cn.smg.luo.smtech_video.VideoActivity;

/**
 * 评论发送对话框
 * @author jl_luo
 * @name: cn.smg.luo.smtech_video.view
 * @description:
 * @date 2016/4/12 10:11
 */

public class CommentDialog extends DialogFragment{
    int mNum;
    @Bind(R.id.et_comment)
    EditText mText;
    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static CommentDialog newInstance(int num) {
        CommentDialog f = new CommentDialog();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        switch ((mNum-1)%6) {
            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
            case 4: style = DialogFragment.STYLE_NORMAL; break;
            case 5: style = DialogFragment.STYLE_NORMAL; break;
            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
            case 8: style = DialogFragment.STYLE_NORMAL; break;
        }
        switch ((mNum-1)%6) {
            case 4: theme = android.R.style.Theme_Holo; break;
            case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
            case 6: theme = android.R.style.Theme_Holo_Light; break;
            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
            case 8: theme = android.R.style.Theme_Holo_Light; break;
        }
        setStyle(style, theme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comment_dialog, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    @OnClick(R.id.tv_send_danmu)
    void clickSendDanmu(){
        Log.e(CommentDialog.class.getSimpleName(),"~~~~~~~~~~~~~~~~~~~~~");
        VideoActivity parent = (VideoActivity) getActivity();
        if(!TextUtils.isEmpty(mText.getText())) {
            parent.addDanmaku(false, mText.getText().toString());
        }
        this.dismiss();
    }
}
