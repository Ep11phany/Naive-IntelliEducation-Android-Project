package com.example.Sumuhandemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import  androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Sumuhandemo.utils.AnalysisUtils;
import com.example.Sumuhandemo.utils.MD5Utils;
import com.example.Sumuhandemo.utils.HttpUtils;
import com.example.Sumuhandemo.bean.User_Info;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Jack on 2018/4/10.
 */

public class ModifyPwdActivity extends AppCompatActivity {
    private TextView tv_main_title;
    private TextView tv_back;
    private TextView tv_hint;
    private ModifyPwdActivity.MyHandler mHandler;
    private EditText et_original_pwd;
    private EditText et_new_pwd;
    private EditText et_new_pwd_again;
    private Button btn_save;
    private String originalPwd;
    private String newPwd;
    private String newPwdAgain;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        userName = AnalysisUtils.readLoginUserName(this);
        mHandler=new ModifyPwdActivity.MyHandler(this);
        init();
    }
    private void init() {
        tv_main_title = ((TextView) findViewById(R.id.tv_main_title));
        tv_main_title.setText("修改密码");
        tv_hint= ((TextView) findViewById(R.id.tv_hint));
        tv_back = ((TextView) findViewById(R.id.tv_back));
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyPwdActivity.this.finish();
            }
        });
        et_original_pwd = (EditText)findViewById(R.id.et_original_pwd);
        et_new_pwd = (EditText)findViewById(R.id.et_new_pwd);
        et_new_pwd_again = (EditText)findViewById(R.id.et_new_pwd_again);
        btn_save = ((Button) findViewById(R.id.btn_save));
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEditString();
                if(TextUtils.isEmpty(originalPwd)){
                    Toast.makeText(ModifyPwdActivity.this, "请输入原始密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(newPwd)){
                    Toast.makeText(ModifyPwdActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(newPwdAgain)){
                    Toast.makeText(ModifyPwdActivity.this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(MD5Utils.md5(newPwd).equals(originalPwd)){
                    Toast.makeText(ModifyPwdActivity.this, "输入新密码与原始密码不能一致", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!newPwd.equals(newPwdAgain)){
                    Toast.makeText(ModifyPwdActivity.this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Message msg = Message.obtain();
                                User_Info ui = new User_Info(userName,"",originalPwd);
                                ui.newPassword=newPwd;
                                msg.obj=ui;
                                mHandler.handleMessage(msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }

    private void modifyPwd(String newPwd) {
        String md5Pwd = MD5Utils.md5(newPwd);
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(userName,md5Pwd);
        editor.commit();
    }


    private void getEditString() {
        originalPwd = et_original_pwd.getText().toString().trim();
        newPwd = et_new_pwd.getText().toString().trim();
        newPwdAgain = et_new_pwd_again.getText().toString().trim();
    }


    private static class MyHandler extends Handler {
        WeakReference<AppCompatActivity> reference;

        public MyHandler(AppCompatActivity activity) {
            reference = new WeakReference<>(activity);
        }
        public void handleMessage(Message msg) {
            ModifyPwdActivity activity = (ModifyPwdActivity) reference.get();
            User_Info ui=(User_Info)msg.obj;
            Map<String,String> mp=new HashMap<String,String>();
            mp.put("name",ui.Username);
            mp.put("oldPassword",MD5Utils.md5(ui.Password));
            mp.put("newPassword",MD5Utils.md5(ui.newPassword));
            String sri=HttpUtils.sendPostRequest(mp,"UTF-8","/api/user/modifiPassword");//
            if(sri!="Failed"){
                try {
                    JSONObject jo = new JSONObject(sri);
                    String MSG=jo.get("msg").toString();
                    if(MSG.equals("Success!")){
                        activity.tv_hint.setText("密码修改成功");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Thread.sleep(3000);
                                    activity.tv_hint.setText("");
                                } catch (Exception e) {
                                }
                            }
                        }).start();
                        Intent intent = new Intent(activity,LoginActivity.class);
                        activity.startActivity(intent);
                        SettingActivity.instance.finish() ;//关闭设置页面 create field 'instance'
                        activity.finish(); //关闭当前页面
                    }
                    else if(MSG.equals("User not found!")){
                        activity.tv_hint.setText("用户名无效");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Thread.sleep(3000);
                                    activity.tv_hint.setText("");
                                } catch (Exception e) {
                                }
                            }
                        }).start();
                    }
                    else if(MSG.equals("Password wrong!")){
                        activity.tv_hint.setText("密码错误，请重新输入");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Thread.sleep(3000);
                                    activity.tv_hint.setText("");
                                } catch (Exception e) {
                                }
                            }
                        }).start();
                    }
                } catch (JSONException e) {
                }
            }
            else{
                activity.tv_hint.setText("网络超时，请稍后重试");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(3000);
                            activity.tv_hint.setText("");
                        } catch (Exception e) {
                        }
                    }
                }).start();
            }
        }
    }



}