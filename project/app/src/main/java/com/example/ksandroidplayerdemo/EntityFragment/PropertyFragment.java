package com.example.ksandroidplayerdemo.EntityFragment;

import android.app.Dialog;
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
public class PropertyFragment extends Fragment{
    public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {

        private List<Item> labelList;
        private List<Item> valueList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView propertyLabel;
            public TextView propertyValue;
            public ViewHolder(View view) {
                super(view);
//                propertyLabel = (TextView) view.findViewById(R.id.propertyLabel);
//                propertyValue = (TextView) view.findViewById(R.id.propertyValue);
            }
        }

        public PropertyAdapter(List<Item> labels, List<Item> values) {
            labelList = labels;
            valueList = values;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_property, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Item label = labelList.get(position);
            holder.propertyLabel.setText(label.getName());
            Item value = valueList.get(position);
            holder.propertyValue.setText(value.getName());
        }

        @Override
        public int getItemCount() {
            return labelList.size();
        }
    }
}
