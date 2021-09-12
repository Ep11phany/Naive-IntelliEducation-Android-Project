package com.example.Sumuhandemo.Fragment;


import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.fastjson.*;
import com.example.Sumuhandemo.LoginActivity;
import com.example.Sumuhandemo.MainActivity;
import com.example.Sumuhandemo.R;
import com.example.Sumuhandemo.Fragment.InstanceListFragment;
import com.example.Sumuhandemo.utils.HttpUtils;
import com.example.Sumuhandemo.utils.TranslationUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.*;

public class SubjectFragment extends Fragment {

    public View view;
    public String Subject;
    private MyHandler myHandler;
    private View loading_block;
    private com.wang.avi.AVLoadingIndicatorView loading_icon;
    private List<Map<String,String>> lst;
    public SubjectFragment(String name) {
        Subject=TranslationUtils.C2E(name);
    }
    public SubjectFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_subject, container, false);
        myHandler=new MyHandler(getActivity());
        loading_block = view.findViewById(R.id.loading_block);
        loading_icon = view.findViewById(R.id.loading_icon);
        new Thread(new Runnable() {
            @Override
            public void run() {
                startLoading();
                try{
                    Message msg = Message.obtain();
                    HashMap<String ,String> hm=new HashMap<String ,String>();
                    hm.put("course",Subject);
                    msg.obj=hm;
                    myHandler.handleMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                endLoading();
            }
        }).start();
        return view;
    }


    private class MyHandler extends Handler {
        WeakReference<FragmentActivity> reference;
        public MyHandler(FragmentActivity activity) {
            reference = new WeakReference<>(activity);
        }
        public void handleMessage(Message msg) {
            Map<String,String> mp=(HashMap)msg.obj;
            String sri= HttpUtils.sendGetRequest(mp,"UTF-8","/api/edukg/instanceRecommend");
            if(sri!="Failed"){
                try {
                    JSONObject jo = (JSONObject) JSONArray.parse(sri);
                    String code=jo.getString("code");
                    lst=new ArrayList<>();
                    List<String> Lst=new ArrayList<>();
                    if(code.equals("200") || code.equals("0")) {
                        Lst=(List<String>) JSONArray.parse(jo.getString("data"));
                    }

                    for(int i=0;i<Lst.size();i++){
                        Map<String ,String> hm=new HashMap<String ,String>();
                        hm.put("subject",Subject);
                        String dt=((JSONObject) JSONArray.parse(Lst.get(i))).getString("data");
                        JSONObject item = (JSONObject) JSONArray.parse(dt);
                        hm.put("category",item.getString("category"));
                        hm.put("label",item.getString("label"));
                        hm.put("uri",item.getString("uri"));
                        List<JSONObject> property=new ArrayList<>();
                        hm.put("image","");
                        if(!item.getString("property").equals("[]")){
                            property=(List<JSONObject>) JSONArray.parse(item.getString("property"));
                            for(int j=0;j<property.size();j++){
                                JSONObject pro=(JSONObject) property.get(j);
                                if(pro.get("predicateLabel").equals("图片")){
                                    hm.put("image",pro.getString("object"));
                                }
                            }
                        }
                        lst.add(hm);
                    }
                    Map<String ,String> hm=new HashMap<String ,String>();
                    hm.put("category","没有更多内容了，以后再来试试吧");
                    hm.put("label","NULL");
                    hm.put("uri","NULL");
                    hm.put("image","");
                    hm.put("subject","NULL");
                    lst.add(hm);
                    getChildFragmentManager().beginTransaction().replace(R.id.content,new InstanceListFragment(lst)).commit();
                    return;
                } catch (JSONException e) {
                }
            }
        }
    }
    private void startLoading(){
        getActivity().runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              loading_block.setVisibility(View.VISIBLE);
                              loading_icon.setVisibility(View.VISIBLE);
                          }
                      }
        );
    }

    private void endLoading(){
        getActivity().runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              loading_block.setVisibility(View.GONE);
                              loading_icon.setVisibility(View.GONE);
                          }
                      }
        );
    }
}
