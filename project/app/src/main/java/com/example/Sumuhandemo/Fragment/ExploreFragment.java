package com.example.Sumuhandemo.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.example.Sumuhandemo.EntityActivity;
import com.example.Sumuhandemo.LinkActivity;
import com.example.Sumuhandemo.utils.HttpUtils;
import com.example.Sumuhandemo.MainActivity;
import com.example.Sumuhandemo.utils.AnalysisUtils;
import com.example.Sumuhandemo.R;
import com.example.Sumuhandemo.bean.Item;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.app.Activity.*;

import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.Subject;

public class ExploreFragment extends Fragment {

    private View view;
    private RelativeLayout exploreLink;
    private RelativeLayout exploreQuestionRecommend;

    public ExploreFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_explore, container, false);
        exploreLink = view.findViewById(R.id.explore_link);
        exploreQuestionRecommend = view.findViewById(R.id.explore_question_recommend);
        exploreLink.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), LinkActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
