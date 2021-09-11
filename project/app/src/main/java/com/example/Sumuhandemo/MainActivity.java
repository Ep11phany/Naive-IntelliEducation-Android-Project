package com.example.Sumuhandemo;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.Sumuhandemo.Fragment.CourseFragment;
import com.example.Sumuhandemo.Fragment.DialogFragment;
import com.example.Sumuhandemo.Fragment.MyinfoFragment;
import com.example.Sumuhandemo.Fragment.ExploreFragment;
import com.example.Sumuhandemo.utils.AnalysisUtils;
import com.example.Sumuhandemo.utils.HttpUtils;
import com.example.Sumuhandemo.utils.TranslationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MainActivity extends FragmentActivity implements View.OnClickListener{
    private TextView tv_main_title;//标题
    private TextView tv_back;//返回按钮
    private RelativeLayout title_bar;
    private RelativeLayout main_body;
    private TextView bottom_bar_text_course;
    private ImageView bottom_bar_image_course;
    private RelativeLayout bottom_bar_course_btn;
    private TextView bottom_bar_text_exercises;
    private ImageView bottom_bar_image_exercises;
    private RelativeLayout bottom_bar_exercises_btn;
    private TextView bottom_bar_text_myinfo;
    private ImageView bottom_bar_image_myinfo;
    private RelativeLayout bottom_bar_myinfo_btn;
    private TextView bottom_bar_text_explore;
    private ImageView bottom_bar_image_explore;
    private RelativeLayout bottom_bar_explore_btn;
    private LinearLayout main_bottom_bar;
    private TextView tv_question;
    private MyHandler myHandler;
    private MyHandler1 myHandler1;
    protected long exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp=getSharedPreferences("HistoryInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("History", "[]");
        initView();
        initInfo();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setMain();
    }

    private void initInfo(){
        myHandler=new MyHandler(this);
        myHandler1=new MyHandler1(this);
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
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Message msg = Message.obtain();
                    HashMap<String ,String> hm=new HashMap<String ,String>();
                    hm.put("name",AnalysisUtils.readLoginUserName(getApplicationContext()));
                    msg.obj=hm;
                    myHandler1.handleMessage(msg);
                } catch (Exception e) {
                }
            }
        }).start();
    }


    //给MainActivity加上退出清除登陆状态的方法。
    // 连续点击返回两次则退出，两次点击间隔超过2秒则提示再按一次退出。
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setMain() {
        this.getSupportFragmentManager().beginTransaction().add(R.id.main_body,new MyinfoFragment()).commit();
        setSelectStatus(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            if (!AnalysisUtils.readLoginStatus(getApplicationContext())){
                Intent Data=new Intent(this.getApplicationContext(), LoginActivity.class);
                //RESULT_OK为Activity系统常量，状态码为-1
                // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                setResult(RESULT_OK,Data);
                //销毁登录界面
                startActivity(Data);
                finish();
                //跳转到主界面，登录成功的状态传递到 MainActivity 中
            }
            else{
                switch (data.getIntExtra("SelectedStatus",0)){
                    case 0:
                        setSelectStatus(0);
                        break;
                    case 1:
                        setSelectStatus(1);
                        break;
                    case 2:
                        setSelectStatus(2);
                        break;
                }
            }
        }
    }

    private void setSelectStatus(int index) {
        switch (index){
            case 0:
                //清除所有页面以重新渲染
                bottom_bar_image_course.setImageResource(R.drawable.main_icon_selected);
                bottom_bar_text_course.setTextColor(Color.parseColor("#0097F7"));
                bottom_bar_text_exercises.setTextColor(Color.parseColor("#666666"));
                bottom_bar_text_myinfo.setTextColor(Color.parseColor("#666666"));
                bottom_bar_text_explore.setTextColor(Color.parseColor("#666666"));
                bottom_bar_image_exercises.setImageResource(R.drawable.main_question_icon);
                bottom_bar_image_myinfo.setImageResource(R.drawable.main_my_icon);
                bottom_bar_image_explore.setImageResource(R.drawable.main_explore_icon);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new CourseFragment()).commit();
                tv_main_title.setText("首页");
                tv_question.setVisibility(View.VISIBLE);
                break;
            case 1:
                bottom_bar_image_exercises.setImageResource(R.drawable.main_question_icon_selected);
                bottom_bar_text_exercises.setTextColor(Color.parseColor("#0097F7"));
                bottom_bar_text_course.setTextColor(Color.parseColor("#666666"));
                bottom_bar_text_myinfo.setTextColor(Color.parseColor("#666666"));
                bottom_bar_text_explore.setTextColor(Color.parseColor("#666666"));
                bottom_bar_image_course.setImageResource(R.drawable.main_icon);
                bottom_bar_image_myinfo.setImageResource(R.drawable.main_my_icon);
                bottom_bar_image_explore.setImageResource(R.drawable.main_explore_icon);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new DialogFragment()).commit();
                tv_main_title.setText("问答");
                tv_question.setVisibility(View.INVISIBLE);
                break;
            case 2:
                bottom_bar_image_myinfo.setImageResource(R.drawable.main_my_icon_selected);
                bottom_bar_text_myinfo.setTextColor(Color.parseColor("#0097F7"));
                bottom_bar_text_course.setTextColor(Color.parseColor("#666666"));
                bottom_bar_text_exercises.setTextColor(Color.parseColor("#666666"));
                bottom_bar_text_explore.setTextColor(Color.parseColor("#666666"));
                bottom_bar_image_exercises.setImageResource(R.drawable.main_question_icon);
                bottom_bar_image_course.setImageResource(R.drawable.main_icon);
                bottom_bar_image_explore.setImageResource(R.drawable.main_explore_icon);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new MyinfoFragment()).commit();
                tv_main_title.setText("我的");
                tv_question.setVisibility(View.INVISIBLE);
                break;
            case 3:
                bottom_bar_image_explore.setImageResource(R.drawable.main_explore_icon_selected);
                bottom_bar_text_course.setTextColor(Color.parseColor("#666666"));
                bottom_bar_text_exercises.setTextColor(Color.parseColor("#666666"));
                bottom_bar_text_myinfo.setTextColor(Color.parseColor("#666666"));
                bottom_bar_text_explore.setTextColor(Color.parseColor("#0097F7"));
                bottom_bar_image_exercises.setImageResource(R.drawable.main_question_icon);
                bottom_bar_image_myinfo.setImageResource(R.drawable.main_my_icon);
                bottom_bar_image_course.setImageResource(R.drawable.main_icon);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new ExploreFragment()).commit();
                tv_main_title.setText("发现");
                tv_question.setVisibility(View.INVISIBLE);
        }
    }
    private void initView() {
        tv_question=findViewById(R.id.tv_question);
        tv_question.setOnClickListener(this);
        tv_question=findViewById(R.id.tv_question);
        tv_question.setVisibility(View.VISIBLE);
        tv_main_title=findViewById(R.id.tv_main_title);
        title_bar=findViewById(R.id.title_bar);
        //底部导航栏
        main_body = findViewById(R.id.main_body);
        bottom_bar_text_course = findViewById(R.id.bottom_bar_text_course);
        bottom_bar_image_course = findViewById(R.id.bottom_bar_image_course);
        bottom_bar_course_btn = findViewById(R.id.bottom_bar_course_btn);
        bottom_bar_text_exercises = findViewById(R.id.bottom_bar_text_exercises);
        bottom_bar_image_exercises = findViewById(R.id.bottom_bar_image_exercises);
        bottom_bar_exercises_btn = findViewById(R.id.bottom_bar_exercises_btn);
        bottom_bar_text_myinfo = findViewById(R.id.bottom_bar_text_myinfo);
        bottom_bar_image_myinfo = findViewById(R.id.bottom_bar_image_myinfo);
        bottom_bar_myinfo_btn = findViewById(R.id.bottom_bar_myinfo_btn);
        bottom_bar_text_explore = findViewById(R.id.bottom_bar_text_explore);
        bottom_bar_image_explore = findViewById(R.id.bottom_bar_image_explore);
        bottom_bar_explore_btn = findViewById(R.id.bottom_bar_explore_btn);
        main_bottom_bar = findViewById(R.id.main_bottom_bar);

        bottom_bar_course_btn.setOnClickListener(this);
        bottom_bar_exercises_btn.setOnClickListener(this);
        bottom_bar_myinfo_btn.setOnClickListener(this);
        bottom_bar_explore_btn.setOnClickListener(this);

        tv_back=findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_main_title.setText("首页");
        title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bottom_bar_course_btn:
                setSelectStatus(0);
                break;
            case R.id.bottom_bar_exercises_btn:
                setSelectStatus(1);
                break;
            case R.id.bottom_bar_myinfo_btn:
                setSelectStatus(2);
                break;
            case R.id.bottom_bar_explore_btn:
                setSelectStatus(3);
                break;
            case R.id.tv_question:
                startActivity(new Intent(this, QuestionActivity.class));
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }


    private class MyHandler extends Handler {
        WeakReference<FragmentActivity> reference;
        public MyHandler(FragmentActivity activity) {
            reference = new WeakReference<>(activity);
        }
        public void handleMessage(Message msg) {
            Map<String,String> mp=(HashMap)msg.obj;
            String sri= HttpUtils.sendGetRequest(mp,"UTF-8","/api/user/showHistory");
            //发送历史记录信息
            if(sri!="Failed"){
                try {
                    JSONObject jo = new JSONObject(sri);
                    String MSG=jo.get("msg").toString();
                    if(MSG.equals("Success!")){
                        String datastring=jo.get("data").toString();
                        SharedPreferences sp=getSharedPreferences("HistoryInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor=sp.edit();
                        if(!datastring.equals("[]")){
                            List<Map<String, String>> datalst=(List<Map<String, String>>) JSONArray.parse(jo.get("data").toString());
                            List<Map<String, String>> lst=new ArrayList<>();
                            for(int i=0;i<datalst.size();i++){
                                HashMap<String ,String> hm=new HashMap<String ,String>();
                                hm.put("label",datalst.get(i).get("instance"));
                                hm.put("category", TranslationUtils.C2E(datalst.get(i).get("subject")));
                                hm.put("subject",TranslationUtils.C2E(datalst.get(i).get("subject")));
                                hm.put("uri","NULL");
                                lst.add(hm);
                            }
                            editor.putString("History", JSON.toJSONString(lst));
                            editor.commit();
                        }
                    }
                } catch (JSONException e) {
                }
            }
        }
    }


    private class MyHandler1 extends Handler {
        WeakReference<FragmentActivity> reference;

        public MyHandler1(FragmentActivity activity) {
            reference = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            Map<String, String> mp = (HashMap) msg.obj;
            String sri = HttpUtils.sendGetRequest(mp, "UTF-8", "/api/user/showFavorite");
            //发送历史记录信息
            if (sri != "Failed") {
                try {
                    JSONObject jo = new JSONObject(sri);
                    String code=jo.get("code").toString();
                    if(code.equals("200")){
                        String datastring = jo.get("data").toString();
                        SharedPreferences sp = getSharedPreferences("FavoriteInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        if (!datastring.equals("[]")) {
                            List<Map<String, String>> datalst = (List<Map<String, String>>) JSONArray.parse(jo.get("data").toString());
                            List<Map<String, String>> lst = new ArrayList<>();
                            for (int i = 0; i < datalst.size(); i++) {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("label", datalst.get(i).get("instance"));
                                hm.put("category", TranslationUtils.C2E(datalst.get(i).get("subject")));
                                hm.put("subject", TranslationUtils.C2E(datalst.get(i).get("subject")));
                                hm.put("uri", "NULL");
                                lst.add(hm);
                            }
                            editor.putString("Favorite", JSON.toJSONString(lst));
                            editor.commit();
                        }
                    }
                } catch (JSONException e) {
                }
            }
        }
    }

}