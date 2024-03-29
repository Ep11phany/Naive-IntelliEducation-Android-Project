package com.example.Sumuhandemo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.Sumuhandemo.LoginActivity;
import com.example.Sumuhandemo.R;
import com.example.Sumuhandemo.SettingActivity;
import com.example.Sumuhandemo.utils.AnalysisUtils;
import com.example.Sumuhandemo.FavoriteActivity;
import com.example.Sumuhandemo.HistoryActivity;
import com.example.Sumuhandemo.NotebookActivity;

public class MyinfoFragment extends Fragment {

    private ImageView iv_head_icon;//
    private TextView tv_user_name;
    private LinearLayout ll_head;
    private RelativeLayout rl_course_history;
    private RelativeLayout rl_setting;
    private RelativeLayout rl_favorite;
    private RelativeLayout rl_notebook;
    //private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myinfo, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_head = (LinearLayout) view.findViewById(R.id.ll_head);
        iv_head_icon = (ImageView) view.findViewById(R.id.iv_head_icon);
        tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
        rl_course_history = (RelativeLayout) view.findViewById(R.id.rl_course_history);
        rl_favorite = (RelativeLayout) view.findViewById(R.id.rl_favorite);
        rl_setting = (RelativeLayout) view.findViewById(R.id.rl_setting);
        rl_notebook = view.findViewById(R.id.rl_notebook);
        setLoginParams(AnalysisUtils.readLoginStatus(getActivity()));
        rl_course_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), HistoryActivity.class);
                    getActivity().startActivityForResult(intent, 1);
            }
        });
        rl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), SettingActivity.class);
                    getActivity().startActivityForResult(intent, 1);

            }
        });
        rl_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), FavoriteActivity.class);
                    getActivity().startActivityForResult(intent, 1);
            }
        });
        rl_notebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotebookActivity.class);
                getActivity().startActivityForResult(intent, 1);
            }
        });
    }

    /**
     * 这个方法用在onViewCreated()，每次初始化这个界面都会启动
     * 通过登录后留在此页面并且立刻刷新用户名会在MainActivity的onActivityResult中处理
     **/
    private void setLoginParams(boolean isLogin) {
        tv_user_name.setText(AnalysisUtils.readLoginUserName(getActivity()));
    }
}
