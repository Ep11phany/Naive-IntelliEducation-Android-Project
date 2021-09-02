package com.example.ksandroidplayerdemo.Fragment;


import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ksandroidplayerdemo.LoginActivity;
import com.example.ksandroidplayerdemo.MainActivity;
import com.example.ksandroidplayerdemo.R;
import com.example.ksandroidplayerdemo.bean.User_Info;
import com.example.ksandroidplayerdemo.utils.HttpUtils;
import com.example.ksandroidplayerdemo.utils.MD5Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class SubjectFragment extends Fragment {
    public TextView Name;
    public View view;
    public String Subject;
    private MyHandler myHandler;
    public SubjectFragment(String name) {
        Subject=name;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_subject, container, false);
        Name=(TextView)view.findViewById(R.id.subject);
        Name.setText(Subject);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Message msg = Message.obtain();
                    HashMap<String ,String> hm=new HashMap<String ,String>();
                    hm.put("course","chinese");
                    hm.put("name","李白");
                    msg.obj=hm;
                    myHandler.handleMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return view;
    }

    private static class MyHandler extends Handler {
        WeakReference<AppCompatActivity> reference;

        public MyHandler(AppCompatActivity activity) {
            reference = new WeakReference<>(activity);
        }
        public void handleMessage(Message msg) {
            LoginActivity activity = (LoginActivity) reference.get();
            User_Info ui=(User_Info)msg.obj;
            Map<String,String> mp=new HashMap<String,String>();
            mp.put("name",ui.Username);
            mp.put("password", MD5Utils.md5(ui.Password));
            String sri= HttpUtils.sendGetRequest(mp,"UTF-8","/api/user/login");//
            if(sri!="Failed"){
                try {
                    JSONObject jo = new JSONObject(sri);
                    String MSG=jo.get("msg").toString();
                    if(MSG.equals("Success!")){
                        //登录成功后关闭此页面进入主页
                        Intent data=new Intent();
                        //datad.putExtra( ); name , value ;
                        data.putExtra("isLogin",true);
                        //RESULT_OK为Activity系统常量，状态码为-1
                        // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                        activity.setResult(RESULT_OK,data);
                        //销毁登录界面
                        activity.finish();
                        //跳转到主界面，登录成功的状态传递到 MainActivity 中
                        activity.startActivity(new Intent(activity, MainActivity.class));
                        return;

                    }

                } catch (JSONException e) {
                }
            }

        }
    }

}
