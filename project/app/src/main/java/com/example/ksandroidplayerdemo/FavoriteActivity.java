package com.example.ksandroidplayerdemo;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import com.example.ksandroidplayerdemo.utils.HttpUtils;
import com.example.ksandroidplayerdemo.Fragment.InstanceListFragment;
import com.alibaba.fastjson.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteActivity extends AppCompatActivity {

    public View view;
    public String Subject="chinese";//暂时调用搜索接口
    private MyHandler myHandler;
    private List<Map<String,String>> lst;
    private TextView tv_main_title;
    private RelativeLayout rl_title_bar;
    private TextView tv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置页面布局 ,注册界面
        setContentView(R.layout.activity_favorite);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myHandler=new MyHandler(this);
        init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Message msg = Message.obtain();
                    HashMap<String ,String> hm=new HashMap<String ,String>();
                    hm.put("course",Subject);
                    hm.put("searchKey","学");//tobe changed
                    msg.obj=hm;
                    myHandler.handleMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void init() {
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("我的收藏");
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoriteActivity.this.finish();
            }
        });
    }

    private class MyHandler extends Handler {
        WeakReference<AppCompatActivity> reference;
        public MyHandler(AppCompatActivity activity) {
            reference = new WeakReference<>(activity);
        }
        public void handleMessage(Message msg) {
            FavoriteActivity activity = (FavoriteActivity) reference.get();
            Map<String,String> mp=(HashMap)msg.obj;
            String sri= HttpUtils.sendGetRequest(mp,"UTF-8","/api/edukg/searchInstance");
            if(sri!="Failed"){
                try {
                    JSONObject jo = new JSONObject(sri);
                    String MSG=jo.get("msg").toString();
                    if(MSG.equals("成功")){
                        String str= jo.get("data").toString();
                        lst = (List<Map<String, String>>) JSONArray.parse(jo.get("data").toString());
                        getSupportFragmentManager().beginTransaction().replace(R.id.content,new InstanceListFragment(lst,Subject)).commit();
                        return;
                    }
                } catch (JSONException e) {
                }
            }
        }
    }

}
