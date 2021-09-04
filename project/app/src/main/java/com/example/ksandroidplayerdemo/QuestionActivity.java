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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ksandroidplayerdemo.Fragment.CourseFragment;
import com.example.ksandroidplayerdemo.Fragment.SubjectFragment;
import com.example.ksandroidplayerdemo.bean.Item;
import com.example.ksandroidplayerdemo.bean.User_Info;
import com.example.ksandroidplayerdemo.utils.HttpUtils;
import com.example.ksandroidplayerdemo.utils.MD5Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity {
    private EditText Question;
    //用户名，密码，再次输入的密码的控件的获取值
    private String question;
    private MyHandler mHandler;
    private Button query;
    private TextView tv_back;
    private RelativeLayout title_bar;
    private String subject="chinese";
    private List<Item> lst = new ArrayList<>();
    private Recycler adapter;
    private RecyclerView recyclerView;
    //标题布局
    private RelativeLayout rl_title_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lst.add(new Item("语文"));
        lst.add(new Item("数学"));
        lst.add(new Item("英语"));
        lst.add(new Item("物理"));
        lst.add(new Item("化学"));
        lst.add(new Item("生物"));
        lst.add(new Item("政治"));
        lst.add(new Item("历史"));
        lst.add(new Item("地理"));


        setContentView(R.layout.activity_question);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mHandler=new MyHandler(this);
        init();
        adapter = new Recycler(lst);
        recyclerView = (RecyclerView) findViewById(R.id.subjects);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void init() {
        //从main_title_bar.xml 页面布局中获取对应的UI控件
        tv_back=findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_bar=findViewById(R.id.title_bar);
        title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        query=findViewById(R.id.query);
        Question=findViewById(R.id.question);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question=Question.getText().toString().trim();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Message msg = Message.obtain();
                            HashMap<String ,String> hm=new HashMap<String ,String>();
                            hm.put("course",subject);
                            hm.put("searchKey",question);
                            msg.obj=hm;
                            mHandler.handleMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }



    private static class MyHandler extends Handler {
        WeakReference<AppCompatActivity> reference;

        public MyHandler(AppCompatActivity activity) {
            reference = new WeakReference<>(activity);
        }
        public void handleMessage(Message msg) {
            RegisterActivity activity = (RegisterActivity) reference.get();
            User_Info ui = (User_Info) msg.obj;
            Map<String, String> mp = new HashMap<String, String>();
            mp.put("name", ui.Username);
            mp.put("password", MD5Utils.md5(ui.Password));
            mp.put("email", ui.Email);
            String sri = HttpUtils.sendPostRequest(mp, "UTF-8", "/api/user/register");
            if (sri != "Failed") {
                try {
                    JSONObject jo = new JSONObject(sri);
                    String MSG = jo.get("msg").toString();
                    if (MSG.equals("Success!")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Thread.sleep(1000);
                                    activity.finish();
                                } catch (Exception e) {
                                }
                            }
                        }).start();
                    }
                } catch (JSONException e) {
                }
            }
        }
    }
    private class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {

        private List<Item> mItemList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView Name;
            public TextView symbol;
            public ViewHolder(View view) {
                super(view);
                Name = (TextView) view.findViewById(R.id.Subject);
                symbol=(TextView) view.findViewById(R.id.symbol);
            }
        }

        public Recycler(List<Item> ItemList) {
            mItemList = ItemList;
        }

        @Override
        public Recycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_button, parent, false);
            Recycler.ViewHolder holder = new Recycler.ViewHolder(view);
            holder.Name.setOnClickListener(new View.OnClickListener() {//对加载的子项注册监听事件
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    //按钮事件
                    String sub=mItemList.get(position).getName();
                    switch (sub){
                        case "语文":
                            subject="chinese";
                            break;
                        case "数学":
                            subject="math";
                            break;
                        case "英语":
                            subject="english";
                            break;
                        case "物理":
                            subject="physics";
                            break;
                        case "化学":
                            subject="chemistry";
                            break;
                        case "生物":
                            subject="biology";
                            break;
                        case "政治":
                            subject="politics";
                            break;
                        case "历史":
                            subject="history";
                            break;
                        case "地理":
                            subject="geo";
                            break;
                    }

                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(Recycler.ViewHolder holder, int position) {
            Item item = mItemList.get(position);
            holder.Name.setText(item.getName());
           holder.symbol.setText("");
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }
    }
}
