package com.example.Sumuhandemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.Sumuhandemo.ExamActivity;
import com.example.Sumuhandemo.bean.Item;

import com.example.Sumuhandemo.R;
import com.example.Sumuhandemo.utils.AnalysisUtils;
import com.example.Sumuhandemo.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import com.alibaba.fastjson.*;
public class NotebookActivity extends AppCompatActivity {
    private List<Map> questionList;

    private RecyclerView questionView;
    private NotebookActivity.QuestionAdapter adapter;
    public View view;
    private TextView tv_main_title;
    private TextView tv_back;
    private RelativeLayout title_bar;
    private NotebookActivity.MyHandler myHandler;

    public NotebookActivity(){questionList = new ArrayList<>();}
    public NotebookActivity(List<Map> qList) {questionList = qList;}

    public class QuestionAdapter extends RecyclerView.Adapter<NotebookActivity.QuestionAdapter.ViewHolder> {

        private List<Map> qList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView questionBody;
            public TextView questionAnswer;
            public ViewHolder(View view) {
                super(view);
                questionBody = (TextView) view.findViewById(R.id.question_body);
                questionAnswer = (TextView) view.findViewById(R.id.question_answer);
            }
        }

        public QuestionAdapter(List<Map> q) {
            qList = q;
        }

        @Override
        public NotebookActivity.QuestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notebook_item, parent, false);
            NotebookActivity.QuestionAdapter.ViewHolder holder = new NotebookActivity.QuestionAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(NotebookActivity.QuestionAdapter.ViewHolder holder, int position) {
            String body = (String)qList.get(position).get("qBody");
            holder.questionBody.setText(body);
            String answer = (String)qList.get(position).get("qAnswer");
            holder.questionAnswer.setText(answer);
            Long id = new Long((Integer)qList.get(position).get("questionID"));
            holder.questionBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NotebookActivity.this, ExamActivity.class);
                    intent.putExtra("qBody", body);
                    intent.putExtra("qAnswer", answer);
                    intent.putExtra("qId", id);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return qList.size();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_back = findViewById(R.id.tv_back);
        title_bar = findViewById(R.id.title_bar);
        tv_back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                NotebookActivity.this.finish();
            }
        });
        myHandler = new MyHandler(this);
        tv_main_title.setText("我的错题本");
        title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("name", AnalysisUtils.readLoginUserName(NotebookActivity.this));
                String sri = HttpUtils.sendGetRequest(hm, "UTF-8", "/api/user/showQuestion");
                msg.obj = sri;
                myHandler.sendMessage(msg);
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
                    if (code.equals("200") || code.equals("0")) {
                        questionList = ((List<Map>) JSONArray.parse(jo.get("data").toString()));
                        adapter = new NotebookActivity.QuestionAdapter(questionList);
                        questionView = (RecyclerView) findViewById(R.id.question_list);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(NotebookActivity.this);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        questionView.setLayoutManager(layoutManager);
                        questionView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                }
            }
        }
    }

}
