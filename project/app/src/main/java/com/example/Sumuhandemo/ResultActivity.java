package com.example.Sumuhandemo;

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
import com.example.Sumuhandemo.Fragment.CourseFragment;
import com.example.Sumuhandemo.Fragment.InstanceListFragment;
import com.example.Sumuhandemo.Fragment.SubjectFragment;
import com.example.Sumuhandemo.bean.Item;
import com.example.Sumuhandemo.bean.User_Info;
import com.example.Sumuhandemo.utils.HttpUtils;
import com.example.Sumuhandemo.utils.MD5Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.content.res.Resources;



public class ResultActivity extends AppCompatActivity {
    //用户名，密码，再次输入的密码的控件的获取值
    private String searchKey;
    private MyHandler mHandler;
    private TextView tv_main_title;
    private TextView tv_back;
    private RelativeLayout title_bar;
    private TextView sort;
    private TextView reverse_sort;
    private String subject;
    private List<Map<String, String>> lst = new ArrayList<>();


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
                Intent data=new Intent();
                data.putExtra("SelectedStatus",1);
                setResult(RESULT_OK,data);
                finish();
            }
        });

        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("“"+searchKey+"”"+"的搜索结果");

        sort=findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(lst, new Comparator<Map<String,String>>() {
                    public int compare(Map<String,String> o1, Map<String,String> o2) {
                        Comparator<Object> com = Collator.getInstance(Locale.CHINA);
                        return  ((Collator) com).compare(o1.get("label"),o2.get("label"));
                    };
                });
                for(int i=0;i<lst.size();i++){
                    lst.get(i).put("subject",subject);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.body, new InstanceListFragment(lst)).commit();
            }
        });
        reverse_sort=findViewById(R.id.reverse_sort);
        reverse_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(lst, new Comparator<Map<String,String>>() {
                    public int compare(Map<String,String> o1, Map<String,String> o2) {
                        Comparator<Object> com = Collator.getInstance(Locale.CHINA);
                        return  ((Collator) com).compare(o2.get("label"),o1.get("label"));
                    };
                });
                for(int i=0;i<lst.size();i++){
                    lst.get(i).put("subject",subject);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.body, new InstanceListFragment(lst)).commit();
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
                    String code=jo.get("code").toString();
                    if(code.equals("200") || code.equals("0")){
                        String str = jo.get("data").toString();
                        lst = (List<Map<String, String>>) JSONArray.parse(jo.get("data").toString());
                        for(int i=0;i<lst.size();i++){
                            lst.get(i).put("subject",subject);
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.body, new InstanceListFragment(lst)).commit();
                        return;
                    }
                } catch (JSONException e) {
                }
            }
        }
    }
}


