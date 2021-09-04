package com.example.ksandroidplayerdemo.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ksandroidplayerdemo.EntityActivity;
import com.example.ksandroidplayerdemo.MainActivity;
import com.example.ksandroidplayerdemo.R;
import com.example.ksandroidplayerdemo.bean.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.app.Activity.*;

import javax.security.auth.Subject;

public class InstanceListFragment extends Fragment {
    private List<Map<String,String>> instanceList;
    private RecyclerView instanceView;
    private Recycler adapter;
    public View view;
    public String subject;
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
            holder.Label.setOnClickListener(new View.OnClickListener() {//对加载的子项注册监听事件
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    //按钮事件

                    Activity activity=getActivity();
                    Intent data=new Intent();
                    //datad.putExtra( ); name , value ;
                    data.putExtra("course", subject);
                    data.putExtra("label", instanceList.get(position).get("label"));
                    //RESULT_OK为Activity系统常量，状态码为-1
                    // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                    activity.setResult(activity.RESULT_OK,data);
                    //销毁登录界面
                    //?activity.finish();
                    //跳转到主界面，登录成功的状态传递到 MainActivity 中
                    activity.startActivity(new Intent(activity, EntityActivity.class));
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

}
