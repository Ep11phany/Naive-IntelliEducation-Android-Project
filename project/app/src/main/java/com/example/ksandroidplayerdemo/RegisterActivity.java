package com.example.ksandroidplayerdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ksandroidplayerdemo.bean.User_Info;
import com.example.ksandroidplayerdemo.utils.HttpUtils;
import com.example.ksandroidplayerdemo.utils.MD5Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private TextView tv_main_title,tv_hint;//标题
    private TextView tv_back;//返回按钮
    private Button btn_register;//注册按钮
    //用户名，密码，再次输入的密码的控件
    private EditText et_user_name,et_email,et_psw,et_psw_again;
    //用户名，密码，再次输入的密码的控件的获取值
    private String userName,psw,pswAgain,email;
    private MyHandler mHandler;
    //标题布局
    private RelativeLayout rl_title_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置页面布局 ,注册界面
        setContentView(R.layout.activity_register);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mHandler=new MyHandler(this);
        init();
    }

    private void init() {
        //从main_title_bar.xml 页面布局中获取对应的UI控件
        tv_main_title=findViewById(R.id.tv_main_title);
        tv_main_title.setText("注册");
        tv_back=findViewById(R.id.tv_back);
        tv_hint=findViewById(R.id.tv_hint);
        //布局根元素
        rl_title_bar=findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.TRANSPARENT);
        //从activity_register.xml 页面中获取对应的UI控件
        btn_register=findViewById(R.id.btn_register);
        et_user_name=findViewById(R.id.et_user_name);
        et_email=findViewById(R.id.et_email);
        et_psw=findViewById(R.id.et_psw);

        et_psw_again=findViewById(R.id.et_psw_again);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回键
                RegisterActivity.this.finish();
            }
        });
        //注册按钮
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入在相应控件中的字符串
                getEditString();
                //判断输入框内容
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(psw)){
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(pswAgain)){
                    Toast.makeText(RegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!psw.equals(pswAgain)){
                    Toast.makeText(RegisterActivity.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Message msg = Message.obtain();
                            msg.obj = new User_Info(userName,email,psw); //借用email传递
                            mHandler.handleMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }
    /**
     * 获取控件中的字符串
     */
    private void getEditString(){
        userName=et_user_name.getText().toString().trim();
        email=et_email.getText().toString().trim();
        psw=et_psw.getText().toString().trim();
        pswAgain=et_psw_again.getText().toString().trim();
    }
    /**
     * 从SharedPreferences中读取输入的用户名，判断SharedPreferences中是否有此用户名
     */
    /**
     * 保存账号和密码到SharedPreferences中SharedPreferences
     */
    private void saveRegisterInfo(String userName){
        //loginInfo表示文件名, mode_private SharedPreferences sp = getSharedPreferences( );
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取编辑器， SharedPreferences.Editor  editor -> sp.edit();
        SharedPreferences.Editor editor=sp.edit();
        //以用户名为key，密码为value保存在SharedPreferences中
        //key,value,如键值对，editor.putString(用户名，密码）;
        editor.putString(userName,"");
        //提交修改 editor.commit();
        editor.commit();
    }


    private static class MyHandler extends Handler {
        WeakReference<AppCompatActivity> reference;

        public MyHandler(AppCompatActivity activity) {
            reference = new WeakReference<>(activity);
        }
        public void handleMessage(Message msg) {
            RegisterActivity activity = (RegisterActivity) reference.get();
            User_Info ui = (User_Info) msg.obj;
            Map<String, String> mp = new HashMap<String, String>();
            mp.put("name", ui.Username);
            mp.put("password", MD5Utils.md5(ui.Password));
            mp.put("email", ui.Email);
            String sri = HttpUtils.sendPostRequest(mp, "UTF-8", "/api/user/register");
            if (sri != "Failed") {
                try {
                    JSONObject jo = new JSONObject(sri);
                    String MSG = jo.get("msg").toString();
                    if (MSG.equals("Success!")) {
                        //把账号、密码和账号标识保存到sp里面
                        /**
                         * 保存账号和密码到SharedPreferences中
                         */
                        activity.saveRegisterInfo(ui.Username);
                        Intent data = new Intent();
                        data.putExtra("userName", activity.userName);
                        activity.setResult(RESULT_OK, data);
                        activity.tv_hint.setText("注册成功");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Thread.sleep(1000);
                                    activity.finish();
                                } catch (Exception e) {
                                }
                            }
                        }).start();
                    }
                    else if(MSG.equals("Name Duplicated!")){
                        activity.tv_hint.setText("注册失败，用户名重复");
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
            }else{
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
