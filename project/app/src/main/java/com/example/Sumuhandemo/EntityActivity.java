package com.example.Sumuhandemo;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.Sumuhandemo.EntityFragment.PropertyFragment;
import com.example.Sumuhandemo.EntityFragment.RelationshipFragment;
import com.example.Sumuhandemo.EntityFragment.QuestionFragment;
import com.example.Sumuhandemo.Fragment.InstanceListFragment;
import com.example.Sumuhandemo.Fragment.SubjectFragment;
import com.example.Sumuhandemo.utils.HttpUtils;
import com.example.Sumuhandemo.utils.AnalysisUtils;
import com.example.Sumuhandemo.utils.MD5Utils;
import com.example.Sumuhandemo.bean.User_Info;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.sina.weibo.sdk.share.WbShareCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.lang.ref.WeakReference;
import java.util.List;

import com.example.Sumuhandemo.bean.Item;
import com.example.Sumuhandemo.utils.TranslationUtils;

import javax.security.auth.Subject;


public class EntityActivity extends FragmentActivity implements View.OnClickListener, WbShareCallback {

    private MyHandler myHandler;
    private MyHandler1 myHandler1;

    private View loading_block;
    private com.wang.avi.AVLoadingIndicatorView loading_icon;
    private TextView tv_entity_title;//标题
    private TextView tv_back_entity;//返回按钮
    private RelativeLayout title_bar;
    private RelativeLayout entity_body;
    private TextView top_bar_text_property;
    private RelativeLayout top_bar_property_btn;
    private TextView top_bar_text_relationship;
    private RelativeLayout top_bar_relationship_btn;
    private TextView top_bar_text_question;
    private RelativeLayout top_bar_question_btn;
    private LinearLayout entity_top_bar;
    private ImageView favorite;
    private ImageView share;
    private ImageView qq;
    private ImageView qqspace;
    private ImageView wechat;
    private ImageView friends;
    private ImageView weibo;
    private boolean isFavorite=false;
    String course;
    String label;

    List<Map<String, String>> propertyList;
    List<Map<String, String>> relationshipList;
    List<Map<String, String>> questionList;
    private IWBAPI mWeiboAPI;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = getIntent();
        course = intent.getStringExtra("course");
        label = intent.getStringExtra("label");
        isFavorite=isFavorite(label,course);
        init();
        myHandler = new MyHandler(this);
        myHandler1 =new MyHandler1(this);
        // init weibo
        /*AuthInfo authInfo = new AuthInfo(this, "3464419790", "https://api.weibo.com/oauth2/default.html", "abc123");
        mWeiboAPI = WBAPIFactory.createWBAPI(this);
        mWeiboAPI.registerApp(this, authInfo);
        mWeiboAPI.setLoggerEnable(true);
        startLoading();*/
        new Thread(new Runnable() {
            public void run() {
                startLoading();
                Message msg = Message.obtain();
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("course", course);
                hm.put("name", label);
                msg.obj = hm;
                myHandler.handleMessage(msg);
                endLoading();
            }
        }).start();
    }



    private boolean isFavorite(String label,String subject){
        SharedPreferences sp=getSharedPreferences("FavoriteInfo", MODE_PRIVATE);
        List<Map<String, String>> l=(List<Map<String, String>>) JSONArray.parse(sp.getString("Favorite","[]"));
        for(int i=0;i<l.size();i++){
            if(l.get(i).get("label").equals(label)&&l.get(i).get("subject").equals(subject))
                return true;
        }
        return false;
    }

    private void init(){
        loading_block = findViewById(R.id.loading_block);
        loading_icon = findViewById(R.id.loading_icon);
        tv_entity_title = findViewById(R.id.tv_entity_title);
        tv_entity_title.setText(label);
        tv_back_entity = findViewById(R.id.tv_back_entity);
        tv_back_entity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntityActivity.this.finish();
            }
        });
        title_bar = findViewById(R.id.entity_title_bar);

        favorite=findViewById(R.id.favorite);
        Resources resources = getResources();
        Drawable favor = resources.getDrawable(R.drawable.favorite);
        Drawable unfavor = resources.getDrawable(R.drawable.myinfo_favorite_icon);
        if(isFavorite){
            favorite.setBackground(favor);
        }
        else{
            favorite.setBackground(unfavor);
        }
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavorite){
                    favorite.setBackground(unfavor);
                }
                else{
                    favorite.setBackground(favor);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Message msg = Message.obtain();
                            HashMap<String ,String> hm=new HashMap<String ,String>();
                            hm.put("name",AnalysisUtils.readLoginUserName(getApplicationContext()));
                            hm.put("instance",label);
                            hm.put("subject",course);
                            msg.obj=hm;
                            myHandler1.handleMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();;
            }
        });

        share=findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });



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



    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.share, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        qq = (ImageView) view.findViewById(R.id.qq);
        qqspace = (ImageView) view.findViewById(R.id.qqspace);
        wechat = (ImageView) view.findViewById(R.id.wechat);
        friends = (ImageView) view.findViewById(R.id.friends);
        weibo = (ImageView) view.findViewById(R.id.weibo);
        weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share("快来看看我学到的新知识吧！", label,label );
            }
        });
        dialog.show();
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
    public void share(String wbContent, String title, String content) {
        WeiboMultiMessage msg = new WeiboMultiMessage();
        TextObject Content = new TextObject(); // 正文
        Content.text = wbContent;
        msg.textObject = Content;
        WebpageObject wobj = new WebpageObject();
        wobj.title = title; // 下方框子标题
        wobj.description = content;
        wobj.actionUrl = ""; // 下方框子内容
        msg.mediaObject = wobj;
        mWeiboAPI.shareMessage(msg, false);
    }

    @Override
    public void onComplete() {
        Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(UiError uiError) {
        Toast.makeText(this, "分享失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, "分享取消", Toast.LENGTH_LONG).show();
    }



    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_bar_property_btn:
                setSelectStatus(0);
                break;
            case R.id.top_bar_relationship_btn:
                setSelectStatus(1);
                break;
            case R.id.top_bar_question_btn:
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
                getSupportFragmentManager().beginTransaction().replace(R.id.entity_body,new PropertyFragment(propertyList)).commit();
                break;
            case 1:
                top_bar_text_property.setTextColor(Color.parseColor("#666666"));
                top_bar_text_relationship.setTextColor(Color.parseColor("#0097F7"));
                top_bar_text_question.setTextColor(Color.parseColor("#666666"));
                getSupportFragmentManager().beginTransaction().replace(R.id.entity_body,new RelationshipFragment(relationshipList)).commit();
                break;
            case 2:
                top_bar_text_property.setTextColor(Color.parseColor("#666666"));
                top_bar_text_relationship.setTextColor(Color.parseColor("#666666"));
                top_bar_text_question.setTextColor(Color.parseColor("#0097F7"));
                if(questionList != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.entity_body, new QuestionFragment(questionList)).commit();
                }
                else {
                    new Thread(new Runnable() {
                        public void run() {
                            startLoading();
                            Message msg = Message.obtain();
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("uriName", label);
                            msg.obj = hm;
                            myHandler.getQuestions(msg);
                            endLoading();
                        }
                    }).start();
                }
                break;
        }
    }

    private class MyHandler extends Handler {
        WeakReference<FragmentActivity> reference;
        public MyHandler(FragmentActivity activity) {
            reference = new WeakReference<>(activity);
        }
        public void handleMessage(Message msg) {
            Map<String,String> mp=(HashMap)msg.obj;
            String sri= HttpUtils.sendGetRequest(mp,"UTF-8","/api/edukg/infoInstance");
            //发送历史记录信息
            if(sri!="Failed"){
                try {
                    JSONObject jo = new JSONObject(sri);
                    String MSG=jo.get("msg").toString();
                    if(MSG.equals("成功")){
                        JSONObject data = jo.getJSONObject("data");
                        propertyList = (List<Map<String, String>>) JSONArray.parse(data.get("property").toString());
                        relationshipList = (List<Map<String, String>>) JSONArray.parse(data.get("content").toString());
                        setSelectStatus(0);
                    }
                } catch (JSONException e) {
                }
            }
            else{
                //TODO: add_Sharedpreferance








            }
        }
        public void getQuestions(Message msg) {
            Map<String,String> mp=(HashMap)msg.obj;
            String sri= HttpUtils.sendGetRequest(mp,"UTF-8","/api/edukg/questionList");
            if(sri!="Failed"){
                try {
                    JSONObject jo = new JSONObject(sri);
                    String code=jo.get("code").toString();
                    if(code.equals("200")){
                        questionList = (List<Map<String, String>>) JSONArray.parse(jo.get("data").toString());
                        getSupportFragmentManager().beginTransaction().replace(R.id.entity_body,new QuestionFragment(questionList)).commit();
                    }
                } catch (JSONException e) {
                }
            }
        }
    }

    private void startLoading(){
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              loading_block.setVisibility(View.VISIBLE);
                              loading_icon.setVisibility(View.VISIBLE);
                          }
                      }
        );
    }

    private void endLoading(){
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              loading_block.setVisibility(View.GONE);
                              loading_icon.setVisibility(View.GONE);
                          }
                      }
        );
    }

    private class MyHandler1 extends Handler {
        WeakReference<FragmentActivity> reference;
        public MyHandler1(FragmentActivity activity) {
            reference = new WeakReference<>(activity);
        }
        public void handleMessage(Message msg) {
            Map<String,String> mp=(HashMap)msg.obj;

            if(isFavorite){
                String sri= HttpUtils.sendGetRequest(mp,"UTF-8","/api/user/deleteFavorite");
                isFavorite=false;
            }
            else{
                String sri= HttpUtils.sendGetRequest(mp,"UTF-8","/api/user/addFavorite");
                isFavorite=true;
            }
            Map<String,String> mp1=new HashMap<>();
            mp1.put("name",AnalysisUtils.readLoginUserName(getApplicationContext()));
            String sri = HttpUtils.sendGetRequest(mp1, "UTF-8", "/api/user/showFavorite");
            if (sri != "Failed") {
                try {
                    JSONObject jo = new JSONObject(sri);
                    String code=jo.get("code").toString();
                    if(code.equals("200")){
                        String datastring = jo.get("data").toString();
                        SharedPreferences sp = getSharedPreferences("FavoriteInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        if (!datastring.equals("[]")) {
                            List<Map<String, String>> datalst = (List<Map<String, String>>) JSONArray.parse(jo.get("data").toString());
                            List<Map<String, String>> lst = new ArrayList<>();
                            for (int i = 0; i < datalst.size(); i++) {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("label", datalst.get(i).get("instance"));
                                hm.put("category", TranslationUtils.C2E(datalst.get(i).get("subject")));
                                hm.put("subject", TranslationUtils.C2E(datalst.get(i).get("subject")));
                                hm.put("uri", "NULL");
                                lst.add(hm);
                            }
                            editor.putString("Favorite", JSON.toJSONString(lst));
                            editor.commit();
                        }
                    }
                } catch (JSONException e) {
                }
            }
        }
    }
}
