package com.example.ksandroidplayerdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.example.ksandroidplayerdemo.Fragment.CourseFragment;
import com.example.ksandroidplayerdemo.Fragment.InstanceListFragment;
import com.example.ksandroidplayerdemo.Fragment.SubjectFragment;
import com.example.ksandroidplayerdemo.bean.Item;
import com.example.ksandroidplayerdemo.bean.User_Info;
import com.example.ksandroidplayerdemo.utils.HttpUtils;
import com.example.ksandroidplayerdemo.utils.MD5Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.content.res.Resources;

public class ResultActivity extends AppCompatActivity {
    private EditText Question;
    //用户名，密码，再次输入的密码的控件的获取值
    private String searchKey;
    private MyHandler mHandler;
    private Button query;
    private TextView tv_back;
    private RelativeLayout title_bar;
    private String subject;
    private List<Map<String, String>> lst = new ArrayList<>();
    private Recycler adapter;
    private RecyclerView recyclerView;
    //标题布局
    private RelativeLayout rl_title_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        mHandler = new MyHandler(this);
        subject = intent.getStringExtra("subject");
        searchKey = intent.getStringExtra("searchKey");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message msg = Message.obtain();
                    HashMap<String, String> hm = new HashMap<String, String>();
                    hm.put("course", subject);
                    hm.put("searchKey", searchKey);
                    msg.obj = hm;
                    mHandler.handleMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mHandler = new MyHandler(this);
        init();
    }

    private void init() {
        //从main_title_bar.xml 页面布局中获取对应的UI控件
        tv_back = findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_bar = findViewById(R.id.title_bar);
        title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
    }


    private class MyHandler extends Handler {
        WeakReference<FragmentActivity> reference;

        public MyHandler(FragmentActivity activity) {
            reference = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            Map<String, String> mp = (HashMap) msg.obj;
            String sri = HttpUtils.sendGetRequest(mp, "UTF-8", "/api/edukg/searchInstance");
            if (sri != "Failed") {
                try {
                    JSONObject jo = new JSONObject(sri);
                    String MSG = jo.get("msg").toString();
                    if (MSG.equals("成功")) {
                        String str = jo.get("data").toString();
                        lst = (List<Map<String, String>>) JSONArray.parse(jo.get("data").toString());
                        getSupportFragmentManager().beginTransaction().replace(R.id.body, new InstanceListFragment(lst, subject)).commit();
                        return;
                    }
                } catch (JSONException e) {
                }
            }
        }
    }
}


