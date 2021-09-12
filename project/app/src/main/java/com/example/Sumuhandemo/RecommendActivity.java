package com.example.Sumuhandemo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.example.Sumuhandemo.EntityFragment.QuestionFragment;
import com.example.Sumuhandemo.Fragment.ExploreFragment;
import com.example.Sumuhandemo.bean.Item;
import com.example.Sumuhandemo.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendActivity extends AppCompatActivity {
    private AppCompatActivity activity=this;
    private View view;
    private MyHandler myHandler;
    private List<Map> response;
    private TextView tv_main_title;
    private TextView tv_back;
    private RelativeLayout title_bar;


    public RecommendActivity() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myHandler=new MyHandler(this);
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_back = findViewById(R.id.tv_back);
        title_bar=findViewById(R.id.title_bar);
        tv_back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                activity.finish();
            }
        });
        tv_main_title.setText("试题推荐");
        title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        Intent intent = getIntent();
        String searchText = intent.getStringExtra("searchText");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message msg = Message.obtain();
                    HashMap<String, String> hm = new HashMap<String, String>();
                    hm.put("label", searchText);
                    String sri = HttpUtils.sendGetRequest(hm, "UTF-8", "/api/edukg/questionRecommend");
                    msg.obj = sri;
                    myHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private class MyHandler extends Handler {
        WeakReference<Activity> reference;
        public MyHandler(Activity activity) {
            reference = new WeakReference<>(activity);
        }
        public void handleMessage(Message msg) {
            String sri = (String) msg.obj;
            if (sri != "Failed") {
                try {
                    JSONObject jo = new JSONObject(sri);
                    String code = jo.get("code").toString();
                    if (code.equals("0")||code.equals("200")) {
                        response = ((List<Map>) JSONArray.parse(jo.get("data").toString()));
                        getSupportFragmentManager().beginTransaction().replace(R.id.body, new QuestionFragment(response)).commit();
                    }
                } catch (JSONException e) {
                }
            }
        }
    }
}
