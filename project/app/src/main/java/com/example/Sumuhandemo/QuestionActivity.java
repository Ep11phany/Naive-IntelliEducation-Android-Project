package com.example.Sumuhandemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Sumuhandemo.Fragment.CourseFragment;
import com.example.Sumuhandemo.Fragment.SubjectFragment;
import com.example.Sumuhandemo.bean.Item;
import com.example.Sumuhandemo.bean.User_Info;
import com.example.Sumuhandemo.utils.HttpUtils;
import com.example.Sumuhandemo.utils.MD5Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.content.res.Resources;

public class QuestionActivity extends AppCompatActivity {
    private EditText Question;
    //用户名，密码，再次输入的密码的控件的获取值
    private String question;
    private AppCompatActivity activity=this;
    private Button query;
    private TextView tv_back;
    private RelativeLayout title_bar;
    private TextView tv_main_title;
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
                Intent data=new Intent();
                data.putExtra("SelectedStatus",1);
                setResult(RESULT_OK,data);
                finish();
            }
        });
        tv_main_title=(TextView)findViewById(R.id.tv_main_title);
        tv_main_title.setText("搜索");
        title_bar=findViewById(R.id.title_bar);
        title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        query=findViewById(R.id.query);
        Question=findViewById(R.id.question);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question=Question.getText().toString().trim();
                if(!question.equals("")){
                    Intent data=new Intent(activity, ResultActivity.class);
                    //datad.putExtra( ); name , value ;
                    data.putExtra("searchKey",question);
                    data.putExtra("subject",subject);
                    //RESULT_OK为Activity系统常量，状态码为-1
                    // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                    activity.setResult(RESULT_OK,data);
                    //销毁登录界面
                    activity.finish();
                    //跳转到主界面，登录成功的状态传递到 MainActivity 中
                    activity.startActivity(data);
                }
                else{
                    Toast.makeText(getApplicationContext(),"请输入搜索内容",Toast.LENGTH_LONG);
                }

            }
        });
    }



    private class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {

        private List<Item> mItemList;
        private int mposition=0;

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
            view.setOnClickListener(new View.OnClickListener() {//对加载的子项注册监听事件
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    //按钮事件
                    mposition=holder.getAdapterPosition();
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
                    }notifyDataSetChanged();

                }


            });
            return holder;
        }


        @Override
        public void onBindViewHolder(Recycler.ViewHolder holder, int position) {
            Item item = mItemList.get(position);
            holder.Name.setText(item.getName());
            holder.symbol.setText("");
            Resources resources = getResources();
            Drawable selected = resources.getDrawable(R.drawable.textview_border_selected);
            Drawable unsel = resources.getDrawable(R.drawable.textview_border);
            if (mposition != position) {
                holder.itemView.setBackground(unsel);
            }else if (mposition ==  position) {
                holder.itemView.setBackground(selected);
            }
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }
    }
}
