package com.example.Sumuhandemo;

import android.content.SharedPreferences;
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

import com.alibaba.fastjson.JSON;
import com.example.Sumuhandemo.utils.HttpUtils;
import com.example.Sumuhandemo.utils.AnalysisUtils;
import com.example.Sumuhandemo.Fragment.InstanceListFragment;
import com.alibaba.fastjson.JSONArray;
import com.example.Sumuhandemo.utils.TranslationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteActivity extends AppCompatActivity {

    public View view;
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
                    hm.put("name",AnalysisUtils.readLoginUserName(getApplicationContext()));
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
            String sri= HttpUtils.sendGetRequest(mp,"UTF-8","/api/user/showFavorite");
            if(sri!="Failed"){
                try {
                    JSONObject jo = new JSONObject(sri);
                    String MSG=jo.get("msg").toString();
                    if(MSG.equals("Success!")){
                        String datastring=jo.get("data").toString();
                        SharedPreferences sp=getSharedPreferences("FavoriteInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor=sp.edit();
                        if(!datastring.equals("[]")){
                            List<Map<String, String>> datalst=(List<Map<String, String>>) JSONArray.parse(jo.get("data").toString());
                            lst=new ArrayList<>();
                            for(int i=0;i<datalst.size();i++){
                                HashMap<String ,String> hm=new HashMap<String ,String>();
                                hm.put("label",datalst.get(i).get("instance"));
                                hm.put("category", TranslationUtils.C2E(datalst.get(i).get("subject")));
                                hm.put("subject",TranslationUtils.C2E(datalst.get(i).get("subject")));
                                hm.put("uri","NULL");
                                lst.add(hm);
                            }
                            editor.putString("Favorite", JSON.toJSONString(lst));
                            editor.commit();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content,new InstanceListFragment(lst)).commit();
                        }

                    }
                } catch (JSONException e) {
                }
            }

        }
    }

}
