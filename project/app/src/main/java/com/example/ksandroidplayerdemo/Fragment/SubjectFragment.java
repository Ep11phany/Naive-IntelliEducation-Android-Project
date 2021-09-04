package com.example.ksandroidplayerdemo.Fragment;


import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.fastjson.JSONArray;
import com.example.ksandroidplayerdemo.LoginActivity;
import com.example.ksandroidplayerdemo.MainActivity;
import com.example.ksandroidplayerdemo.R;
import com.example.ksandroidplayerdemo.Fragment.InstanceListFragment;
import com.example.ksandroidplayerdemo.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

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
    private List<Map<String,String>> lst;
    public SubjectFragment(String name) {
        Subject=name;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_subject, container, false);
        myHandler=new MyHandler(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Message msg = Message.obtain();
                    HashMap<String ,String> hm=new HashMap<String ,String>();
                    hm.put("course","chinese");//tobe changed
                    hm.put("searchKey","学");//tobe changed
                    msg.obj=hm;
                    myHandler.handleMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            String sri= HttpUtils.sendGetRequest(mp,"UTF-8","/api/edukg/searchInstance");
            if(sri!="Failed"){
                try {
                    JSONObject jo = new JSONObject(sri);
                    String MSG=jo.get("msg").toString();
                    if(MSG.equals("成功")){
                        String str= jo.get("data").toString();
                        lst = (List<Map<String, String>>) JSONArray.parse(jo.get("data").toString());
                        getChildFragmentManager().beginTransaction().replace(R.id.content,new InstanceListFragment(lst)).commit();
                        return;
                    }
                } catch (JSONException e) {
                }
            }
        }
    }
}
