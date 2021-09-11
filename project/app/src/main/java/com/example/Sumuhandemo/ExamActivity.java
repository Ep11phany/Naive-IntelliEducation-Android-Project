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
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.*;

public class ExamActivity extends AppCompatActivity {

    AppCompatActivity activity = this;
    private String qBody;
    private String qAnswer;
    private ExamActivity.MyHandler myHandler;
    private TextView examBody;
    private EditText examInput;
    private TextView examAnswer;
    private Button examSubmit;
    private TextView tv_main_title;
    private TextView tv_back;
    private RelativeLayout title_bar;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = getIntent();
        qBody = intent.getStringExtra("qBody");
        qAnswer = intent.getStringExtra("qAnswer");
        myHandler = new ExamActivity.MyHandler(this);
        examBody = findViewById(R.id.exam_body);
        examAnswer = findViewById(R.id.exam_answer);
        examSubmit = findViewById(R.id.exam_submit);
        examInput = findViewById(R.id.exam_input);
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_back = findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        examSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        title_bar = findViewById(R.id.title_bar);
        tv_main_title.setText("做题");
        title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        String pattern = "[ABCDEFGHIJKLMNOPQRSTUVWXYZ]+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(qAnswer);
        if(m.find()){
            qAnswer = m.group(1);
        }
        examBody.setText(qBody);
        examAnswer.setText(qAnswer);
    }

    private class MyHandler extends Handler {
        WeakReference<AppCompatActivity> reference;
        public MyHandler(AppCompatActivity activity) {
            reference = new WeakReference<>(activity);
        }
    }
}
