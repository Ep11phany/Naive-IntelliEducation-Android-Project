package com.example.Sumuhandemo.EntityFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.Sumuhandemo.EntityActivity;
import com.example.Sumuhandemo.Fragment.InstanceListFragment;
import com.example.Sumuhandemo.Fragment.SubjectFragment;
import com.example.Sumuhandemo.bean.Item;

import com.example.Sumuhandemo.R;
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
public class PropertyFragment extends Fragment{

    private List<Map<String,String>> propertyList;

    private RecyclerView propertyView;
    private PropertyFragment.PropertyAdapter adapter;
    public View view;

    public PropertyFragment(List<Map<String, String>> pList) {propertyList = pList;}

    public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {

        private List<Map<String,String>> pList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView propertyLabel;
            public TextView propertyValue;
            public ViewHolder(View view) {
                super(view);
                propertyLabel = (TextView) view.findViewById(R.id.property_label);
                propertyValue = (TextView) view.findViewById(R.id.property_value);
            }
        }

        public PropertyAdapter(List<Map<String, String>> p) {
            pList = p;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_property_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String label = pList.get(position).get("predicateLabel");
            holder.propertyLabel.setText(label);
            String value = pList.get(position).get("object");
            holder.propertyValue.setText(value);
        }

        @Override
        public int getItemCount() {
            return pList.size();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entity_property, container, false);
        adapter = new PropertyFragment.PropertyAdapter(propertyList);
        propertyView = (RecyclerView) view.findViewById(R.id.property_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        propertyView.setLayoutManager(layoutManager);
        propertyView.setAdapter(adapter);
        return view;
    }
}
