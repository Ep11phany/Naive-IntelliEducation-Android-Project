package com.example.Sumuhandemo.EntityFragment;

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
public class RelationshipFragment extends Fragment {
    private List<Map<String,String>> relationshipList;

    private RecyclerView relationshipView;
    private RelationshipFragment.RelationshipAdapter adapter;
    public View view;

    public RelationshipFragment(List<Map<String, String>> rList) {relationshipList = rList;}

    public class RelationshipAdapter extends RecyclerView.Adapter<RelationshipFragment.RelationshipAdapter.ViewHolder> {

        private List<Map<String,String>> rList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView relationshipLabel;
            public TextView relationshipValue;
            public TextView relationshipType;
            public ViewHolder(View view) {
                super(view);
                relationshipLabel = (TextView) view.findViewById(R.id.relationship_label);
                relationshipValue = (TextView) view.findViewById(R.id.relationship_value);
                relationshipType = (TextView) view.findViewById(R.id.relationship_type);
            }
        }

        public RelationshipAdapter(List<Map<String, String>> r) {
            rList = r;
        }

        @Override
        public RelationshipFragment.RelationshipAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_relationship_item, parent, false);
            RelationshipFragment.RelationshipAdapter.ViewHolder holder = new RelationshipFragment.RelationshipAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RelationshipFragment.RelationshipAdapter.ViewHolder holder, int position) {
            Map<String, String> map = rList.get(position);
            String label = map.get("predicate_label");
            holder.relationshipLabel.setText(label);

            if(map.containsKey("subject_label")){
                String subject = map.get("subject_label");
                holder.relationshipValue.setText(subject);
                holder.relationshipType.setText("to");
            }
            else if(map.containsKey("object_label")){
                String object = map.get("object_label");
                holder.relationshipValue.setText(object);
                holder.relationshipType.setText("from");
            }
        }

        @Override
        public int getItemCount() {
            return rList.size();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entity_relationship, container, false);
        adapter = new RelationshipFragment.RelationshipAdapter(relationshipList);
        relationshipView = (RecyclerView) view.findViewById(R.id.relationship_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        relationshipView.setLayoutManager(layoutManager);
        relationshipView.setAdapter(adapter);
        return view;
    }
}
