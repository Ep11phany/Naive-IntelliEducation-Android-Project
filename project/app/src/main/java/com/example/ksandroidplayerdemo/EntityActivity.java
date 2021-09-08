package com.example.ksandroidplayerdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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

import com.alibaba.fastjson.JSONArray;
import com.example.ksandroidplayerdemo.EntityFragment.PropertyFragment;
import com.example.ksandroidplayerdemo.EntityFragment.RelationshipFragment;
import com.example.ksandroidplayerdemo.EntityFragment.QuestionFragment;
import com.example.ksandroidplayerdemo.Fragment.InstanceListFragment;
import com.example.ksandroidplayerdemo.Fragment.SubjectFragment;
import com.example.ksandroidplayerdemo.utils.HttpUtils;
import com.example.ksandroidplayerdemo.utils.MD5Utils;
import com.example.ksandroidplayerdemo.bean.User_Info;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.lang.ref.WeakReference;
import java.util.List;

import com.example.ksandroidplayerdemo.bean.Item;


public class EntityActivity extends FragmentActivity implements View.OnClickListener{

    private EntityActivity.MyHandler myHandler;

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

    String course;
    String label;

    List<Map<String, String>> propertyList;
    List<Map<String, String>> relationshipList;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = getIntent();
        course = intent.getStringExtra("course");
        label = intent.getStringExtra("label");
        init();
        myHandler = new MyHandler(this);
        new Thread(new Runnable() {
            public void run() {
                Message msg = Message.obtain();
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("course", course);
                hm.put("name", label);
                msg.obj = hm;
                myHandler.handleMessage(msg);

            }
        }).start();
    }

    private void init(){
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
                getSupportFragmentManager().beginTransaction().replace(R.id.entity_body,new QuestionFragment()).commit();
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
            HttpUtils.sendGetRequest(mp,"UTF-8","/api/edukg/infoInstance");
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
    }
}
