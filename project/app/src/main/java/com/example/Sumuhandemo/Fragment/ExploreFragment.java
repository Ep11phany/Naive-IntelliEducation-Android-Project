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
  
    public class Recycler1 extends RecyclerView.Adapter<ExploreFragment.Recycler1.ViewHolder> {

        private List<Item> mItemList;
        private int mposition=0;
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView Name;
            public ViewHolder(View view) {
                super(view);
                Name = (TextView) view.findViewById(R.id.Subject_Label);
            }
        }

        public Recycler1(List<Item> ItemList) {
            mItemList = ItemList;
        }

        @Override
        public ExploreFragment.Recycler1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjet_label, parent, false);
            ExploreFragment.Recycler1.ViewHolder holder = new ExploreFragment.Recycler1.ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {//对加载的子项注册监听事件
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();

                    //按钮事件
                    String sub=mItemList.get(position).getName();
                    mposition=holder.getAdapterPosition();
                    switch (sub){
                        case "语文":
                            subject="chinese";
                            break;
                        case "数学":
                            subject="math";
                            break;
                        case "英语":
                            subject="english";
                            break;
                        case "物理":
                            subject="physics";
                            break;
                        case "化学":
                            subject="chemistry";
                            break;
                        case "生物":
                            subject="biology";
                            break;
                        case "政治":
                            subject="politics";
                            break;
                        case "历史":
                            subject="history";
                            break;
                        case "地理":
                            subject="geo";
                            break;
                        //按钮事件
                    }notifyDataSetChanged();
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ExploreFragment.Recycler1.ViewHolder holder, int position) {
            Item item = mItemList.get(position);
            holder.Name.setText(item.getName());
            if (mposition != position) {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }else if (mposition ==  position) {
                holder.itemView.setBackgroundColor(Color.parseColor("#30B4FF"));
            }
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }
    }

    private class MyHandler extends Handler {
        WeakReference<FragmentActivity> reference;
        public MyHandler(FragmentActivity activity) {
            reference = new WeakReference<>(activity);
        }
        public void handleMessage(Message msg) {
            String sri = (String) msg.obj;
            if (sri != "Failed") {
                try {
                    JSONObject jo = new JSONObject(sri);
                    String code=jo.get("code").toString();
                    if(code.equals("200")){
                        response = ((List<Map>) JSONArray.parse(jo.getJSONObject("data").get("results").toString()));
                        SpannableString output = new SpannableString(searchText);
                        for(Map map : response){
                            output.setSpan(new MyClickableSpan((String)map.get("entity")), (int)map.get("start_index"), (int)map.get("end_index") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        linkOutput.setText(output);
                        linkOutput.setMovementMethod(LinkMovementMethod.getInstance());
                        linkOutput.setHighlightColor(Color.TRANSPARENT);
                    }
                } catch (JSONException e) {
                }
            }
        }
    }
}
