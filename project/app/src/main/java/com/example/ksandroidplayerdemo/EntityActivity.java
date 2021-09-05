package com.example.ksandroidplayerdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ksandroidplayerdemo.EntityFragment.PropertyFragment;
import com.example.ksandroidplayerdemo.EntityFragment.RelationshipFragment;
import com.example.ksandroidplayerdemo.EntityFragment.QuestionFragment;
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


public class EntityActivity extends FragmentActivity implements View.OnClickListener{

    private TextView tv_main_title;//标题
    private TextView tv_back;//返回按钮
    private RelativeLayout title_bar;
    private RelativeLayout entity_body;
    private TextView top_bar_text_property;
    private RelativeLayout top_bar_property_btn;
    private TextView top_bar_text_relationship;
    private RelativeLayout top_bar_relationship_btn;
    private TextView top_bar_text_question;
    private RelativeLayout top_bar_question_btn;
    private LinearLayout entity_top_bar;



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
        init();
    }

    private void init(){
        tv_main_title=findViewById(R.id.tv_main_title);
        title_bar=findViewById(R.id.title_bar);

        entity_body = findViewById(R.id.entity_body);
        top_bar_text_property = findViewById(R.id.top_bar_text_property);
        top_bar_property_btn = findViewById(R.id.top_bar_property_btn);
        top_bar_text_relationship = findViewById(R.id.top_bar_text_relationship);
        top_bar_relationship_btn = findViewById(R.id.top_bar_relationship_btn);
        top_bar_text_question = findViewById(R.id.top_bar_text_question);
        top_bar_question_btn = findViewById(R.id.top_bar_question_btn);
        entity_top_bar = findViewById(R.id.entity_top_bar);

        top_bar_property_btn.setOnClickListener(this);
        top_bar_relationship_btn.setOnClickListener(this);
        top_bar_question_btn.setOnClickListener(this);
        //tv_main_title.setText("课程");
        title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_bar_property_btn:
                //getSupportFragmentManager().beginTransaction().add(R.id.main_body,new CourseFragment()).commit();
                setSelectStatus(0);
                break;
            case R.id.top_bar_relationship_btn:
                //getSupportFragmentManager().beginTransaction().add(R.id.main_body,new ExercisesFragment()).commit();
                setSelectStatus(1);
                break;
            case R.id.top_bar_question_btn:
                //getSupportFragmentManager().beginTransaction().add(R.id.main_body,new MyinfoFragment()).commit();
                setSelectStatus(2);
                break;
        }
    }

    private void setSelectStatus(int index) {
        switch (index){
            case 0:
                top_bar_text_property.setTextColor(Color.parseColor("#0097F7"));
                top_bar_text_relationship.setTextColor(Color.parseColor("#666666"));
                top_bar_text_question.setTextColor(Color.parseColor("#666666"));
                getSupportFragmentManager().beginTransaction().replace(R.id.entity_body,new PropertyFragment()).commit();
                break;
            case 1:
                top_bar_text_property.setTextColor(Color.parseColor("#666666"));
                top_bar_text_relationship.setTextColor(Color.parseColor("#0097F7"));
                top_bar_text_question.setTextColor(Color.parseColor("#666666"));
                getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new RelationshipFragment()).commit();
                break;
            case 2:
                top_bar_text_property.setTextColor(Color.parseColor("#666666"));
                top_bar_text_relationship.setTextColor(Color.parseColor("#666666"));
                top_bar_text_question.setTextColor(Color.parseColor("#0097F7"));
                getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new QuestionFragment()).commit();
                break;
        }
    }
}
