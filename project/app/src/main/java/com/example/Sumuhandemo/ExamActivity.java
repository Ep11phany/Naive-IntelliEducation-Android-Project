package com.example.Sumuhandemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import androidx.appcompat.app.AlertDialog;
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
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.*;

public class ExamActivity extends AppCompatActivity {

    AppCompatActivity activity = this;
    private String qBody;
    private String oriAnswer;
    private String qAnswer;
    private Long qId;
    private ExamActivity.MyHandler myHandler;
    private TextView examBody;
    private EditText examInput;
    private TextView examAnswer;
    private TextView examCorrectAnswer;
    private TextView examTf;
    private Button examRedo;
    private Button examSubmit;
    private TextView tv_main_title;
    private TextView tv_back;
    private RelativeLayout title_bar;
    com.getbase.floatingactionbutton.AddFloatingActionButton floatButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = getIntent();
        qBody = intent.getStringExtra("qBody");
        oriAnswer = intent.getStringExtra("qAnswer");
        qId = intent.getLongExtra("qId", 0);
        myHandler = new ExamActivity.MyHandler(this);
        examBody = findViewById(R.id.exam_body);
        examAnswer = findViewById(R.id.exam_answer);
        examSubmit = findViewById(R.id.exam_submit);
        examInput = findViewById(R.id.exam_input);
        examTf = findViewById(R.id.exam_tf);
        examRedo = findViewById(R.id.exam_redo);
        examCorrectAnswer = findViewById(R.id.exam_correct_answer);
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_back = findViewById(R.id.tv_back);
        floatButton = findViewById(R.id.float_button);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(ExamActivity.this);
                alertdialogbuilder.setMessage("确定将此题加入错题本？");
                alertdialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = Message.obtain();
                                HashMap hm = new HashMap();
                                hm.put("questionID", qId.toString());
                                hm.put("qBody", qBody);
                                hm.put("qAnswer", oriAnswer);
                                hm.put("name", AnalysisUtils.readLoginUserName(ExamActivity.this));
                                msg.obj = hm;
                                myHandler.handleMessage(msg);
                            }
                        }).start();
                        //Toast.makeText(ExamActivity.this, "yes"+qId, Toast.LENGTH_LONG).show();
                    }
                });
                alertdialogbuilder.setNeutralButton("取消", null);
                final AlertDialog alertdialog1 = alertdialogbuilder.create();
                alertdialog1.show();
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        examSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                examAnswer.setVisibility(View.VISIBLE);
                examCorrectAnswer.setVisibility(View.VISIBLE);
                String t = examInput.getText().toString().trim();
                if (t.equals(qAnswer)) {
                    examTf.setText("回答正确！");
                } else {
                    examTf.setText("回答错误！");
                }
                examTf.setVisibility(View.VISIBLE);
                examSubmit.setEnabled(false);
                examInput.setEnabled(false);
            }
        });
        examRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                examAnswer.setVisibility(View.GONE);
                examCorrectAnswer.setVisibility(View.GONE);
                examTf.setVisibility(View.GONE);
                examSubmit.setEnabled(true);
                examInput.setEnabled(true);
                ((TextView) examInput).setText("");
            }
        });
        title_bar = findViewById(R.id.title_bar);
        tv_main_title.setText("做题");
        title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        String pattern = "[ABCDEFGHIJKLMNOPQRSTUVWXYZ]+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(oriAnswer);
        if (m.find()) {
            qAnswer = m.group();
        }
        else{
            qAnswer = oriAnswer;
        }
        examBody.setText(qBody);
        examAnswer.setText(qAnswer);
    }

    private class MyHandler extends Handler {
        WeakReference<AppCompatActivity> reference;

        public MyHandler(AppCompatActivity activity) {
            reference = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            Map<String, String> mp = (HashMap) msg.obj;
            String sri = HttpUtils.sendGetRequest(mp, "UTF-8", "/api/user/addQuestion");
            //发送历史记录信息
            if (sri != "Failed") {
                try {
                    JSONObject jo = new JSONObject(sri);
                    String code = jo.get("code").toString();
                    if (code.equals("200") || code.equals("0")) {
                        Looper.prepare();
                        Toast.makeText(ExamActivity.this, "添加成功！", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                    else if(code.equals("404")){
                        Looper.prepare();
                        Toast.makeText(ExamActivity.this, "这道题目已经添加到错题本啦！", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (JSONException e) {
                }
            }
            else{
                Looper.prepare();
                Toast.makeText(ExamActivity.this, sri, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }
    }
}
