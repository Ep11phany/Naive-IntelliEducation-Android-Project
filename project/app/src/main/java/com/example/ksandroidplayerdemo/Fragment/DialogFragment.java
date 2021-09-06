package com.example.ksandroidplayerdemo.Fragment;


import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.TypedValue;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.ksandroidplayerdemo.ResultActivity;
import com.example.ksandroidplayerdemo.bean.Item;

import com.example.ksandroidplayerdemo.R;
import com.example.ksandroidplayerdemo.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import com.alibaba.fastjson.*;
import android.content.Context;
import android.view.View.MeasureSpec;

public class DialogFragment extends Fragment {

    private RecyclerView recyclerView;
    private View view;
    private Recycler adapter;
    private Recycler1 radapter;
    private EditText editText;
    private List<Pair<String,String>> Dialogs=new ArrayList<>();
    private ImageView Enter;
    private MyHandler mHandler;
    private String subject="chinese";
    private List<Item>SubjectList=new ArrayList<>();

    public DialogFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SubjectList.add(new Item("语文"));
        SubjectList.add(new Item("数学"));
        SubjectList.add(new Item("英语"));
        SubjectList.add(new Item("物理"));
        SubjectList.add(new Item("化学"));
        SubjectList.add(new Item("生物"));
        SubjectList.add(new Item("政治"));
        SubjectList.add(new Item("历史"));
        SubjectList.add(new Item("地理"));
        mHandler=new MyHandler(getActivity());
        Dialogs.add(new Pair<String,String>("ai","如果有问题，请随时提问我哦！"));
        adapter=new Recycler(Dialogs);
        view = inflater.inflate(R.layout.fragment_dialog, container, false);
        editText=(EditText)view.findViewById(R.id.user_word);
        Enter=(ImageView) view.findViewById(R.id.enter);
        setRecyclerView();
        Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question=editText.getText().toString().trim();
                editText.setText("");
                Dialogs.add(new Pair<String,String>("user",question));
                setRecyclerView();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Message msg = Message.obtain();
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("course", subject);
                            hm.put("inputQuestion", question);
                            msg.obj = hm;
                            mHandler.handleMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        radapter = new Recycler1(SubjectList);
        recyclerView = (RecyclerView) view.findViewById(R.id.subjects);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(radapter);
        return view;
    }


    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager   设置RecyclerView对应的manager
     * @param mRecyclerView  当前的RecyclerView
     * @param n  要跳转的位置
     */
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }
    }

    private void setRecyclerView(){
        adapter=new Recycler(Dialogs);
        recyclerView = (RecyclerView) view.findViewById(R.id.dialogs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        MoveToPosition((LinearLayoutManager) recyclerView.getLayoutManager(),recyclerView,Dialogs.size());
        recyclerView.scrollToPosition(Dialogs.size()-1);
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


    private class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {

        private List<Pair<String,String>> mItemList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView words;
            public String side;
            public ViewHolder(View view) {
                super(view);
                words = (TextView) view.findViewById(R.id.bubble);
            }
        }







        public Recycler(List<Pair<String,String>> ItemList) {
            mItemList = ItemList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //tobe changed
            View view;
            if(viewType==0){
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_bubble, parent, false);
            }else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ai_bubble, parent, false);
            }

            ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {//对加载的子项注册监听事件
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    //按钮事件
                }
            });
            return holder;
        }
        @Override
        public int getItemViewType(int position){
            if(mItemList.get(position).first=="user"){
                return 0;
            }
            return 1;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Pair<String,String> pair=mItemList.get(position);
            holder.words.setText(pair.second);
            holder.side= pair.first;

        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }
    }
    private class MyHandler extends Handler {
        WeakReference<FragmentActivity> reference;

        public MyHandler(FragmentActivity activity) {
            reference = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            Map<String, String> mp = (HashMap) msg.obj;
            String sri = HttpUtils.sendPostRequest(mp, "UTF-8", "/api/edukg/qa");
            if (sri != "Failed") {
                try {
                    JSONObject jo = new JSONObject(sri);
                    String MSG = jo.get("msg").toString();
                    if (MSG.equals("成功")) {
                        String str = jo.get("data").toString();
                        Map<String, String> map = ((List<Map<String, String>>) JSONArray.parse(jo.get("data").toString())).get(0);
                        Dialogs.add(new Pair<String,String>("ai",map.get("value")));
                        setRecyclerView();
                        return;
                    }
                } catch (JSONException e) {
                }
            }
        }
    }
}
