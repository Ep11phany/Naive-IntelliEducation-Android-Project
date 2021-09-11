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

public class LinkActivity extends AppCompatActivity {
    private AppCompatActivity activity=this;
    private RecyclerView recyclerView;
    private View view;
    private String searchText;
    private EditText linkInput;
    private Button linkBtn;
    private TextView linkOutput;
    private LinkActivity.MyHandler myHandler;
    private List<Item> subjectList;
    private LinkActivity.Recycler1 radapter;
    private String subject = "chinese";
    private List<Map> response;
    private TextView tv_main_title;
    private TextView tv_back;
    private RelativeLayout title_bar;

    class MyClickableSpan extends ClickableSpan {

        String keyword;

        MyClickableSpan(String keyword) {
            super();
            this.keyword = keyword;
        }

        @Override
        public void onClick(@NonNull View view) {
            Intent data=new Intent(activity, EntityActivity.class);
            data.putExtra("course", subject);
            data.putExtra("label", keyword);
            startActivity(data);
        }
        public void updateDrawState(TextPaint ds){
            ds.setColor(Color.BLUE);
            ds.setUnderlineText(true);
        }
    }

    public LinkActivity() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subjectList = new ArrayList<>();
        subjectList.add(new Item("语文"));
        subjectList.add(new Item("数学"));
        subjectList.add(new Item("英语"));
        subjectList.add(new Item("物理"));
        subjectList.add(new Item("化学"));
        subjectList.add(new Item("生物"));
        subjectList.add(new Item("政治"));
        subjectList.add(new Item("历史"));
        subjectList.add(new Item("地理"));
        setContentView(R.layout.activity_link);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_back = findViewById(R.id.tv_back);
        title_bar=findViewById(R.id.title_bar);
        tv_back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                activity.finish();
            }
        });
        tv_main_title.setText("知识点链接");
        title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));



        recyclerView = findViewById(R.id.subjects);
        radapter = new LinkActivity.Recycler1(subjectList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(radapter);
        linkInput = findViewById(R.id.link_input);
        linkOutput = findViewById(R.id.link_output);
        linkBtn = findViewById(R.id.link_btn);
        myHandler = new LinkActivity.MyHandler(this);
        linkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = linkInput.getText().toString().trim();
                if(!searchText.equals("")){
                    //linkInput.setText("");
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Message msg = Message.obtain();
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("course", subject);
                                hm.put("context", searchText);
                                String sri = HttpUtils.sendPostRequest(hm, "UTF-8", "/api/edukg/linkInstance");
                                msg.obj = sri;
                                myHandler.sendMessage(msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }

    public class Recycler1 extends RecyclerView.Adapter<Recycler1.ViewHolder> {

        private List<Item> mItemList;
        private int mposition=0;
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView Name;
            public ViewHolder(View view) {
                super(view);
                Name = (TextView) view.findViewById(R.id.Subject_Label);
            }
        }

        public Recycler1(List<Item> ItemList) {
            mItemList = ItemList;
        }

        @Override
        public Recycler1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjet_label, parent, false);
            Recycler1.ViewHolder holder = new Recycler1.ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {//对加载的子项注册监听事件
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();

                    //按钮事件
                    String sub=mItemList.get(position).getName();
                    mposition=holder.getAdapterPosition();
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
                        //按钮事件
                    }notifyDataSetChanged();
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(Recycler1.ViewHolder holder, int position) {
            Item item = mItemList.get(position);
            holder.Name.setText(item.getName());
            if (mposition != position) {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }else if (mposition ==  position) {
                holder.itemView.setBackgroundColor(Color.parseColor("#30B4FF"));
            }
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }
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
                    String MSG = jo.get("msg").toString();
                    if (MSG.equals("成功")) {
                        response = ((List<Map>) JSONArray.parse(jo.getJSONObject("data").get("results").toString()));
                        SpannableString output = new SpannableString(searchText);
                        for(Map map : response){
                            output.setSpan(new LinkActivity.MyClickableSpan((String)map.get("entity")), (int)map.get("start_index"), (int)map.get("end_index") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        linkOutput.setText(output);
                        linkOutput.setMovementMethod(LinkMovementMethod.getInstance());
                        linkOutput.setHighlightColor(Color.TRANSPARENT);
                    }
                } catch (JSONException e) {
                }
            }
        }
    }
}
