package com.example.Sumuhandemo.Fragment;


import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
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


public class CourseFragment extends Fragment implements View.OnClickListener{

    private List<Item> SubjectList;
    private List<Item> toadd_SubjectList=new ArrayList<>();
    private List<Item> total_SubjectList=new ArrayList<>();
    private TextView plus_btn;
    private RecyclerView recyclerView;
    private RecyclerView currentView;
    private RecyclerView toadd_View;
    private View view;
    private Recycler1 radapter;
    private Recycler adapter;
    private Recycler toadd_adapter;
    private ViewPager SubViewPager;
    private ImageView refresh;
    private List<Fragment> SubFragments=new ArrayList<>();

    public CourseFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return SubFragments.get(position);
        }

        public long getItemId(int position){
            return SubjectList.get(position).getName().hashCode();
        }

        @Override
        public int getCount() {
            return SubFragments.size();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_course, container, false);
        plus_btn=(TextView)view.findViewById(R.id.plus_btn);
        SubViewPager = (ViewPager) view.findViewById(R.id.subpage);

        initSubject();
        SubViewPager.setOffscreenPageLimit(SubFragments.size());
        SubViewPager.setAdapter(new MyFragmentAdapter(getChildFragmentManager()));
        SubViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //position只有当页面真正切换的时候，才会调用这个方法，如果页面index一直没有变，则不会调用改方法
                radapter.mposition=position;
                radapter.notifyDataSetChanged();
            }
        });
        radapter = new Recycler1(SubjectList);
        recyclerView = (RecyclerView) view.findViewById(R.id.viewpage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(radapter);


        refresh=(ImageView) view.findViewById(R.id.refresh);
        refresh.setAlpha(0.5f);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubViewPager.setAdapter(new MyFragmentAdapter(getChildFragmentManager()));
            }
        });

        setListener(view);
        return view;
    }


    private void initSubject(){
        total_SubjectList.add(new Item("语文"));
        total_SubjectList.add(new Item("数学"));
        total_SubjectList.add(new Item("英语"));
        total_SubjectList.add(new Item("物理"));
        total_SubjectList.add(new Item("化学"));
        total_SubjectList.add(new Item("生物"));
        total_SubjectList.add(new Item("政治"));
        total_SubjectList.add(new Item("历史"));
        total_SubjectList.add(new Item("地理"));

        SharedPreferences sp=getActivity().getSharedPreferences("SubjectInfo", MODE_PRIVATE);
        SubjectList=new ArrayList<>();
        SharedPreferences.Editor editor=sp.edit();
        if(sp.getBoolean("FirstTime?",true)){
            //若为第一次，默认加载三科
            SubjectList.add(new Item("语文"));
            SubjectList.add(new Item("数学"));
            SubjectList.add(new Item("英语"));
            editor.putBoolean("语文",true);
            editor.putBoolean("数学",true);
            editor.putBoolean("英语",true);
            toadd_SubjectList.add(new Item("物理"));
            toadd_SubjectList.add(new Item("化学"));
            toadd_SubjectList.add(new Item("生物"));
            toadd_SubjectList.add(new Item("政治"));
            toadd_SubjectList.add(new Item("历史"));
            toadd_SubjectList.add(new Item("地理"));
            editor.putBoolean("FirstTime?",false);
            editor.commit();
        }else {
            if (sp.getBoolean("语文", false)) {
                SubjectList.add(new Item("语文"));
            } else {
                toadd_SubjectList.add(new Item("语文"));
            }
            if (sp.getBoolean("数学", false)) {
                SubjectList.add(new Item("数学"));
            } else {
                toadd_SubjectList.add(new Item("数学"));
            }
            if (sp.getBoolean("英语", false)) {
                SubjectList.add(new Item("英语"));
            } else {
                toadd_SubjectList.add(new Item("英语"));
            }
            if (sp.getBoolean("物理", false)) {
                SubjectList.add(new Item("物理"));
            } else {
                toadd_SubjectList.add(new Item("物理"));
            }
            if (sp.getBoolean("化学", false)) {
                SubjectList.add(new Item("化学"));
            } else {
                toadd_SubjectList.add(new Item("化学"));
            }
            if (sp.getBoolean("生物", false)) {
                SubjectList.add(new Item("生物"));
            } else {
                toadd_SubjectList.add(new Item("生物"));
            }
            if (sp.getBoolean("政治", false)) {
                SubjectList.add(new Item("政治"));
            } else {
                toadd_SubjectList.add(new Item("政治"));
            }
            if (sp.getBoolean("历史", false)) {
                SubjectList.add(new Item("历史"));
            } else {
                toadd_SubjectList.add(new Item("历史"));
            }
            if (sp.getBoolean("地理", false)) {
                SubjectList.add(new Item("地理"));
            } else {
                toadd_SubjectList.add(new Item("地理"));
            }
        }

        SubFragments=new ArrayList<>();
        Iterator<Item> iterator = total_SubjectList.iterator();
        while (iterator.hasNext()){
            Item next = iterator.next();
            String name = next.getName();
            if (isIn(name,SubjectList)){
                SubFragments.add(new SubjectFragment(name));
            }
        }
    }


    boolean isIn(String sub,List<Item> lst){
        Iterator<Item> iterator = lst.iterator();
        while (iterator.hasNext()){
            Item next = iterator.next();
            String name = next.getName();
            if (name.equals(sub)){
                return true;
            }
        }
        return false;
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plus_btn:
                showBottomDialog();
                break;
        }
    }

    private void setListener(View v) {
        plus_btn.setOnClickListener(this);
    }

    private class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {

        private List<Item> mItemList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView Name;
            public TextView symbol;
            public ViewHolder(View view) {
                super(view);
                Name = (TextView) view.findViewById(R.id.Subject);
                symbol=(TextView) view.findViewById(R.id.symbol);
            }
        }

        public Recycler(List<Item> ItemList) {
            mItemList = ItemList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_button, parent, false);
            ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {//对加载的子项注册监听事件
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    String sub=mItemList.get(position).getName();

                    //按钮事件
                    SubFragments=new ArrayList<>();
                    Iterator<Item> iterator = total_SubjectList.iterator();
                    List<Item> lst1=new ArrayList<>();
                    List<Item> lst2=new ArrayList<>();
                    //将展示列表信息存入sharepreference
                    SharedPreferences sp=getActivity().getSharedPreferences("SubjectInfo", MODE_PRIVATE);
                    //获取编辑器
                    SharedPreferences.Editor editor=sp.edit();
                    //从total_Subject中全部读入，重新记录，并非移动，若有更好的方式可以移动并保持学科顺序，请修改
                    while (iterator.hasNext()){
                        Item next = iterator.next();
                        String name = next.getName();
                        if ((isIn(name,SubjectList)&&!name.equals(sub))||(isIn(name,toadd_SubjectList)&&name.equals(sub))){
                            lst1.add(new Item(name));
                            SubFragments.add(new SubjectFragment(name));
                            //存入编辑器
                            editor.putBoolean(name,true);
                        }
                        else{
                            lst2.add(new Item(name));
                            //存入编辑器
                            editor.putBoolean(name,false);
                        }
                    }
                    //提交修改
                    editor.commit();
                    SubjectList= lst1;
                    toadd_SubjectList=lst2;
                    SubViewPager.setAdapter(new MyFragmentAdapter(getFragmentManager()));
                    radapter=new Recycler1(SubjectList);
                    recyclerView.setAdapter(radapter);
                    SubViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            //position只有当页面真正切换的时候，才会调用这个方法，如果页面index一直没有变，则不会调用改方法
                            radapter.mposition=position;
                            radapter.notifyDataSetChanged();
                        }
                    });
                    setListener(view);
                    initBottom();
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Item item = mItemList.get(position);
            holder.Name.setText(item.getName());
            if(isIn(item.getName(), SubjectList)){
                holder.symbol.setText("-");
            }
            else{
                holder.symbol.setText("+");
            }
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }
    }

    public class Recycler1 extends RecyclerView.Adapter<Recycler1.ViewHolder> {

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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjet_label, parent, false);
            ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {//对加载的子项注册监听事件
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    String sub=mItemList.get(position).getName();
                    //按钮事件
                    mposition=holder.getAdapterPosition();
                    SubViewPager.setCurrentItem(position);
                    notifyDataSetChanged();
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
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

    private void initBottom(){
        adapter=new Recycler(SubjectList);
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(),4);
        currentView.setLayoutManager(layoutManager);
        currentView.setAdapter(adapter);
        toadd_adapter =new Recycler(toadd_SubjectList);
        layoutManager = new GridLayoutManager(view.getContext(),4);
        toadd_View.setLayoutManager(layoutManager);
        toadd_View.setAdapter(toadd_adapter);
    }

    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this.getContext(), R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this.getContext(), R.layout.add_subject, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        toadd_View = (RecyclerView) view.findViewById(R.id.toadd);
        currentView = (RecyclerView) view.findViewById(R.id.current);
        initBottom();
        dialog.show();
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

}
