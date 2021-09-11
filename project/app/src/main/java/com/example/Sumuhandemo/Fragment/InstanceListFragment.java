package com.example.Sumuhandemo.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.Sumuhandemo.EntityActivity;
import com.example.Sumuhandemo.utils.HttpUtils;
import com.example.Sumuhandemo.utils.AnalysisUtils;
import com.example.Sumuhandemo.R;
import com.example.Sumuhandemo.utils.TranslationUtils;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class InstanceListFragment extends Fragment {
    private List<Map<String,String>> instanceList;
    private RecyclerView instanceView;
    private Recycler adapter;
    public View view;
    private MyHandler myHandler;
    public Bitmap bitmap;
    public InstanceListFragment(List l) {
        instanceList=l;
    }


    private boolean contain(List<Map<String,String>> l,String label,String subject){
        for(int i=0;i<l.size();i++){
            if(l.get(i).get("label").equals(label)&&l.get(i).get("subject").equals(subject))
                return true;
        }
        return false;
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
            public ImageView Image;
            public ViewHolder(View view,int type) {
                super(view);
                Label = (TextView) view.findViewById(R.id.label);
                Catagory = (TextView) view.findViewById(R.id.category);
                if(type==1){
                    Image=(ImageView) view.findViewById(R.id.image);
                }
            }
        }

        public Recycler(List<Map<String,String>> ItemList) {
            mInstanceList = ItemList;
        }

        @Override
        public Recycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Recycler.ViewHolder holder;
            View view;
            if(viewType==0){
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instance_item, parent, false);
                holder = new Recycler.ViewHolder(view,viewType);
            }
            else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instance_item_img, parent, false);
                holder = new Recycler.ViewHolder(view,viewType);
            }

            view.setOnClickListener(new View.OnClickListener() {//对加载的子项注册监听事件
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    //按钮事件
                    if(!instanceList.get(position).get("label").equals("NULL")){
                        Activity activity=getActivity();
                        Intent data=new Intent(activity, EntityActivity.class);
                        data.putExtra("course", instanceList.get(position).get("subject"));
                        data.putExtra("label", instanceList.get(position).get("label"));
                        activity.setResult(activity.RESULT_OK,data);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Message msg = Message.obtain();
                                    HashMap<String ,String> hm=new HashMap<String ,String>();
                                    hm.put("name",AnalysisUtils.readLoginUserName(getActivity().getApplicationContext()));
                                    hm.put("instance",instanceList.get(position).get("label"));
                                    hm.put("subject",instanceList.get(position).get("subject"));
                                    msg.obj=hm;
                                    myHandler.handleMessage(msg);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        holder.Label.setTextColor(Color.parseColor("#AAAAAA"));
                        holder.Catagory.setTextColor(Color.parseColor("#AAAAAA"));
                        //销毁登录界面
                        //?activity.finish();
                        //跳转到主界面，登录成功的状态传递到 MainActivity 中
                        activity.startActivity(data);
                    }

                }
            });
            return holder;
        }

        @Override
        public int getItemViewType(int position) {
            Map<String ,String> map = mInstanceList.get(position);
            if (map.containsKey("image")&& !map.get("image").equals("")) {
                //返回尾部的对应的类型
                return 1;//有图
            } else {
                return 0;//无图
            }
        }


        @Override
        public void onBindViewHolder(Recycler.ViewHolder holder, int position) {
            Map<String ,String> map = mInstanceList.get(position);

            holder.Catagory.setText(TranslationUtils.E2C(map.get("category")));
            holder.Label.setText(map.get("label"));
            if(getItemViewType(position)==1){
                setNetworkBitmap(holder.Image,map.get("image"));
            }
            SharedPreferences sp=getActivity().getSharedPreferences("HistoryInfo", MODE_PRIVATE);
            List<Map<String, String>> lst=(List<Map<String, String>>) JSONArray.parse(sp.getString("History","[]"));


            if(map.get("label").equals("NULL")) {
                holder.Label.setVisibility(View.GONE);
                holder.Catagory.setTextColor(Color.parseColor("#AAAAAA"));
                holder.Catagory.setTextSize(25);
            }
            else{
                holder.Label.setVisibility(View.VISIBLE);
                holder.Catagory.setTextSize(15);
                if(contain(lst,map.get("label"),map.get("subject"))){
                    holder.Label.setTextColor(Color.parseColor("#AAAAAA"));
                    holder.Catagory.setTextColor(Color.parseColor("#AAAAAA"));
                }
                else{
                    holder.Label.setTextColor(Color.parseColor("#000000"));
                    holder.Catagory.setTextColor(Color.parseColor("#000000"));
                }
            }
            if(map.get("category").equals("")){
                holder.Catagory.setVisibility(View.GONE);
            }
            else{
                holder.Catagory.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return mInstanceList.size();
        }
    }


    public void setNetworkBitmap(ImageView im,String url) {

        Runnable networkImg = new Runnable() {
            @Override
            public void run() {
                try {
                    URL httpUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                    conn.setConnectTimeout(6000);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    InputStream in = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(networkImg).start();
        while(bitmap == null)
            continue;
        im.setImageBitmap(bitmap);
    }



    private class MyHandler extends Handler {
        WeakReference<FragmentActivity> reference;
        public MyHandler(FragmentActivity activity) {
            reference = new WeakReference<>(activity);
        }
        public void handleMessage(Message msg) {
            Map<String,String> mp=(HashMap)msg.obj;
            String sri= HttpUtils.sendGetRequest(mp,"UTF-8","/api/user/addHistory");
            Map<String ,String> mp1=new HashMap<>();
            mp1.put("name",AnalysisUtils.readLoginUserName(getActivity().getApplicationContext()));
            sri= HttpUtils.sendGetRequest(mp1,"UTF-8","/api/user/showHistory");
            //发送历史记录信息
            if(sri!="Failed"){
                try {
                    JSONObject jo = new JSONObject(sri);
                    String code=jo.get("code").toString();
                    if(code.equals("200")){
                        String datastring=jo.get("data").toString();
                        SharedPreferences sp=getActivity().getSharedPreferences("HistoryInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor=sp.edit();
                        if(!datastring.equals("[]")){
                            List<Map<String, String>> datalst=(List<Map<String, String>>) JSONArray.parse(jo.get("data").toString());
                            List<Map<String, String>> lst=new ArrayList<>();
                            for(int i=0;i<datalst.size();i++){
                                HashMap<String ,String> hm=new HashMap<String ,String>();
                                hm.put("label",datalst.get(i).get("instance"));
                                hm.put("category", TranslationUtils.C2E(datalst.get(i).get("subject")));
                                hm.put("subject",TranslationUtils.C2E(datalst.get(i).get("subject")));
                                hm.put("uri","NULL");
                                lst.add(hm);
                            }
                            editor.putString("History", JSON.toJSONString(lst));
                            editor.commit();
                        }
                    }
                } catch (JSONException e) {
                }
            }
        }
    }

}
