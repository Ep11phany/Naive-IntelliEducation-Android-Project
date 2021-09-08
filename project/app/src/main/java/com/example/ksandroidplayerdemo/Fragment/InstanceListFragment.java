package com.example.ksandroidplayerdemo.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.example.ksandroidplayerdemo.EntityActivity;
import com.example.ksandroidplayerdemo.utils.HttpUtils;
import com.example.ksandroidplayerdemo.MainActivity;
import com.example.ksandroidplayerdemo.utils.AnalysisUtils;
import com.example.ksandroidplayerdemo.R;
import com.example.ksandroidplayerdemo.bean.Item;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.app.Activity.*;

import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.Subject;

public class InstanceListFragment extends Fragment {
    private List<Map<String,String>> instanceList;
    private RecyclerView instanceView;
    private Recycler adapter;
    public View view;
    public String subject;
    private MyHandler myHandler;
    public InstanceListFragment(List l,String s) {
        instanceList=l;
        subject=s;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_instancelist, container, false);
        adapter=new Recycler(instanceList);
        instanceView=(RecyclerView) view.findViewById(R.id.InstanceList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        instanceView.setLayoutManager(layoutManager);
        instanceView.setAdapter(adapter);

        myHandler = new MyHandler(getActivity());
        return view;
    }

    private class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {

        private List<Map<String,String>> mInstanceList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView Label;
            public TextView Catagory;
            public ViewHolder(View view) {
                super(view);
                Label = (TextView) view.findViewById(R.id.label);
                Catagory = (TextView) view.findViewById(R.id.category);
            }
        }

        public Recycler(List<Map<String,String>> ItemList) {
            mInstanceList = ItemList;
        }

        @Override
        public Recycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instance_item, parent, false);
            Recycler.ViewHolder holder = new Recycler.ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {//对加载的子项注册监听事件
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    //按钮事件

                    Activity activity=getActivity();
                    Intent data=new Intent(activity, EntityActivity.class);
                    data.putExtra("course", subject);
                    data.putExtra("label", instanceList.get(position).get("label"));
                    activity.setResult(activity.RESULT_OK,data);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Message msg = Message.obtain();
                                HashMap<String ,String> hm=new HashMap<String ,String>();
                                hm.put("name",AnalysisUtils.readLoginUserName(getActivity().getApplicationContext()));
                                hm.put("url",instanceList.get(position).get("uri"));//tobe changed
                                msg.obj=hm;
                                myHandler.handleMessage(msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    //销毁登录界面
                    //?activity.finish();
                    //跳转到主界面，登录成功的状态传递到 MainActivity 中
                    activity.startActivity(data);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(Recycler.ViewHolder holder, int position) {
            Map<String ,String> map = mInstanceList.get(position);
            holder.Label.setText(map.get("label"));
            holder.Catagory.setText(map.get("category"));
        }

        @Override
        public int getItemCount() {
            return mInstanceList.size();
        }
    }


    private class MyHandler extends Handler {
        WeakReference<FragmentActivity> reference;
        public MyHandler(FragmentActivity activity) {
            reference = new WeakReference<>(activity);
        }
        public void handleMessage(Message msg) {
            Map<String,String> mp=(HashMap)msg.obj;
            String sri= HttpUtils.sendGetRequest(mp,"UTF-8","/api/user/addHistory");
        }
    }
}
