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

public class InputActivity extends AppCompatActivity {
    private AppCompatActivity activity=this;
    private View view;
    private String searchText;
    private EditText Input;
    private Button Btn;
    private MyHandler myHandler;
    private String subject = "chinese";
    private List<Map<String,String>> response;
    private TextView tv_main_title;
    private TextView tv_back;
    private RelativeLayout title_bar;


    public InputActivity() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

        Input = findViewById(R.id.input);

        Btn = findViewById(R.id.btn);
        myHandler = new MyHandler(this);
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = Input.getText().toString().trim();
                if(!searchText.equals("")){
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    Intent data=new Intent(getApplicationContext(),RecommendActivity.class);
                    data.putExtra("searchText",searchText);
                    activity.startActivity(data);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "请输入内容", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                        response = ((List<Map<String,String>>) JSONArray.parse(jo.get("data").toString()));

                    }
                } catch (JSONException e) {
                }
            }
        }
    }
}
