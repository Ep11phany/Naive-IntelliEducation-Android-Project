package com.example.Sumuhandemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.Sumuhandemo.EntityFragment.PropertyFragment;
import com.example.Sumuhandemo.EntityFragment.RelationshipFragment;
import com.example.Sumuhandemo.EntityFragment.QuestionFragment;
import com.example.Sumuhandemo.Fragment.InstanceListFragment;
import com.example.Sumuhandemo.Fragment.SubjectFragment;
import com.example.Sumuhandemo.utils.HttpUtils;
import com.example.Sumuhandemo.utils.AnalysisUtils;
import com.example.Sumuhandemo.utils.MD5Utils;
import com.example.Sumuhandemo.bean.User_Info;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.lang.ref.WeakReference;
import java.util.List;

import com.example.Sumuhandemo.bean.Item;
import com.example.Sumuhandemo.utils.TranslationUtils;

import javax.security.auth.Subject;


public class EntityActivity extends FragmentActivity implements View.OnClickListener{

    private MyHandler myHandler;
    private MyHandler1 myHandler1;

    private View loading_block;
    private com.wang.avi.AVLoadingIndicatorView loading_icon;
    private TextView tv_entity_title;//标题
    private TextView tv_back_entity;//返回按钮
    private RelativeLayout title_bar;
    private RelativeLayout entity_body;
    private TextView top_bar_text_property;
    private RelativeLayout top_bar_property_btn;
    private TextView top_bar_text_relationship;
    private RelativeLayout top_bar_relationship_btn;
    private TextView top_bar_text_question;
    private RelativeLayout top_bar_question_btn;
    private LinearLayout entity_top_bar;
    private ImageView favorite;
    private boolean isFavorite=false;
    String course;
    String label;

    List<Map<String, String>> propertyList;
    List<Map<String, String>> relationshipList;
    List<Map<String, String>> questionList;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = getIntent();
        course = intent.getStringExtra("course");
        label = intent.getStringExtra("label");
        isFavorite=isFavorite(label,course);
        init();
        myHandler = new MyHandler(this);
        myHandler1 =new MyHandler1(this);
        startLoading();
        new Thread(new Runnable() {
            public void run() {
                startLoading();
                Message msg = Message.obtain();
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("course", course);
                hm.put("name", label);
                msg.obj = hm;
                myHandler.handleMessage(msg);
                endLoading();
            }
        }).start();
    }



    private boolean isFavorite(String label,String subject){
        SharedPreferences sp=getSharedPreferences("FavoriteInfo", MODE_PRIVATE);
        List<Map<String, String>> l=(List<Map<String, String>>) JSONArray.parse(sp.getString("Favorite","[]"));
        for(int i=0;i<l.size();i++){
            if(l.get(i).get("label").equals(label)&&l.get(i).get("subject").equals(subject))
                return true;
        }
        return false;
    }

    private void init(){
        loading_block = findViewById(R.id.loading_block);
        loading_icon = findViewById(R.id.loading_icon);
        tv_entity_title = findViewById(R.id.tv_entity_title);
        tv_entity_title.setText(label);
        tv_back_entity = findViewById(R.id.tv_back_entity);
        tv_back_entity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntityActivity.this.finish();
            }
        });
        title_bar = findViewById(R.id.entity_title_bar);

        favorite=findViewById(R.id.favorite);
        Resources resources = getResources();
        Drawable favor = resources.getDrawable(R.drawable.favorite);
        Drawable unfavor = resources.getDrawable(R.drawable.myinfo_favorite_icon);
        if(isFavorite){
            favorite.setBackground(favor);
        }
        else{
            favorite.setBackground(unfavor);
        }
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavorite){
                    favorite.setBackground(unfavor);

                }
                else{
                    favorite.setBackground(favor);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Message msg = Message.obtain();
                            HashMap<String ,String> hm=new HashMap<String ,String>();
                            hm.put("name",AnalysisUtils.readLoginUserName(getApplicationContext()));
                            hm.put("instance",label);
                            hm.put("subject",course);
                            msg.obj=hm;
                            myHandler1.handleMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();;
            }
        });
        entity_body = findViewById(R.id.entity_body);
        top_bar_text_property = findViewById(R.id.top_bar_text_property);
        top_bar_property_btn = findViewById(R.id.top_bar_property_btn);
        top_bar_text_relationship = findViewById(R.id.top_bar_text_relationship);
        top_bar_relationship_btn = findViewById(R.id.top_bar_relationship_btn);
        top_bar_text_question = findViewById(R.id.top_bar_text_question);
        top_bar_question_btn = findViewById(R.id.top_bar_question_btn);
        entity_top_bar = findViewById(R.id.entity_top_bar);

        top_bar_property_btn.setOnClickListener(this);
        top_bar_relationship_btn.setOnClickListener(this);
        top_bar_question_btn.setOnClickListener(this);
        //tv_main_title.setText("课程");
        title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_bar_property_btn:
                setSelectStatus(0);
                break;
            case R.id.top_bar_relationship_btn:
                setSelectStatus(1);
                break;
            case R.id.top_bar_question_btn:
                setSelectStatus(2);
                break;
        }
    }

    private void setSelectStatus(int index) {
        switch (index){
            case 0:
                top_bar_text_property.setTextColor(Color.parseColor("#0097F7"));
                top_bar_text_relationship.setTextColor(Color.parseColor("#666666"));
                top_bar_text_question.setTextColor(Color.parseColor("#666666"));
                getSupportFragmentManager().beginTransaction().replace(R.id.entity_body,new PropertyFragment(propertyList)).commit();
                break;
            case 1:
                top_bar_text_property.setTextColor(Color.parseColor("#666666"));
                top_bar_text_relationship.setTextColor(Color.parseColor("#0097F7"));
                top_bar_text_question.setTextColor(Color.parseColor("#666666"));
                getSupportFragmentManager().beginTransaction().replace(R.id.entity_body,new RelationshipFragment(relationshipList)).commit();
                break;
            case 2:
                top_bar_text_property.setTextColor(Color.parseColor("#666666"));
                top_bar_text_relationship.setTextColor(Color.parseColor("#666666"));
                top_bar_text_question.setTextColor(Color.parseColor("#0097F7"));
                if(questionList != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.entity_body, new QuestionFragment(questionList)).commit();
                }
                else {
                    new Thread(new Runnable() {
                        public void run() {
                            startLoading();
                            Message msg = Message.obtain();
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("uriName", label);
                            msg.obj = hm;
                            myHandler.getQuestions(msg);
                            endLoading();
                        }
                    }).start();
                }
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
            String sri= HttpUtils.sendGetRequest(mp,"UTF-8","/api/edukg/infoInstance");
            //发送历史记录信息
            if(sri!="Failed"){
                try {
                    JSONObject jo = new JSONObject(sri);
                    String MSG=jo.get("msg").toString();
                    if(MSG.equals("成功")){
                        JSONObject data = jo.getJSONObject("data");
                        propertyList = (List<Map<String, String>>) JSONArray.parse(data.get("property").toString());
                        relationshipList = (List<Map<String, String>>) JSONArray.parse(data.get("content").toString());
                        setSelectStatus(0);
                    }
                } catch (JSONException e) {
                }
            }
            else{
                //TODO: add_Sharedpreferance








            }
        }
        public void getQuestions(Message msg) {
            Map<String,String> mp=(HashMap)msg.obj;
            String sri= HttpUtils.sendGetRequest(mp,"UTF-8","/api/edukg/questionList");
            if(sri!="Failed"){
                try {
                    JSONObject jo = new JSONObject(sri);
                    String MSG=jo.get("msg").toString();
                    if(MSG.equals("成功")){
                        questionList = (List<Map<String, String>>) JSONArray.parse(jo.get("data").toString());
                        getSupportFragmentManager().beginTransaction().replace(R.id.entity_body,new QuestionFragment(questionList)).commit();
                    }
                } catch (JSONException e) {
                }
            }
        }
    }

    private void startLoading(){
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              loading_block.setVisibility(View.VISIBLE);
                              loading_icon.setVisibility(View.VISIBLE);
                          }
                      }
        );
    }

    private void endLoading(){
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              loading_block.setVisibility(View.GONE);
                              loading_icon.setVisibility(View.GONE);
                          }
                      }
        );
    }

    private class MyHandler1 extends Handler {
        WeakReference<FragmentActivity> reference;
        public MyHandler1(FragmentActivity activity) {
            reference = new WeakReference<>(activity);
        }
        public void handleMessage(Message msg) {
            Map<String,String> mp=(HashMap)msg.obj;

            if(isFavorite){
                String sri= HttpUtils.sendGetRequest(mp,"UTF-8","/api/user/deleteFavorite");
                isFavorite=false;
            }
            else{
                String sri= HttpUtils.sendGetRequest(mp,"UTF-8","/api/user/addFavorite");
                isFavorite=true;
            }
            Map<String,String> mp1=new HashMap<>();
            mp1.put("name",AnalysisUtils.readLoginUserName(getApplicationContext()));
            String sri = HttpUtils.sendGetRequest(mp1, "UTF-8", "/api/user/showFavorite");
            if (sri != "Failed") {
                try {
                    JSONObject jo = new JSONObject(sri);
                    String MSG = jo.get("msg").toString();
                    if (MSG.equals("Success!")) {
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
