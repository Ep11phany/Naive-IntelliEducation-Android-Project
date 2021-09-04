package com.example.ksandroidplayerdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ksandroidplayerdemo.Fragment.CourseFragment;
import com.example.ksandroidplayerdemo.Fragment.SubjectFragment;
import com.example.ksandroidplayerdemo.utils.HttpUtils;
import com.example.ksandroidplayerdemo.utils.MD5Utils;
import com.example.ksandroidplayerdemo.bean.User_Info;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.lang.ref.WeakReference;
import java.util.List;

import com.example.ksandroidplayerdemo.bean.Item;


public class EntityActivity extends AppCompatActivity{
    private TextView entityName;
    private String entityUri;
    private List<Item> propertyLabel;
    private List<Item> propertyValue;
    private List<Item> propertyUri;
    private List<Item> relationshipLabel;
    private List<Item> relationshipUri;
    private List<Item> otherKind;           //subject or object
    private List<Item> otherName;
    private List<Item> otherUri;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Init();
    }

    private void Init(){

    }

    public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {

        private List<Item> labelList;
        private List<Item> valueList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView propertyLabel;
            public TextView propertyValue;
            public ViewHolder(View view) {
                super(view);
                propertyLabel = (TextView) view.findViewById(R.id.propertyLabel);
                propertyValue = (TextView) view.findViewById(R.id.propertyValue);
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
