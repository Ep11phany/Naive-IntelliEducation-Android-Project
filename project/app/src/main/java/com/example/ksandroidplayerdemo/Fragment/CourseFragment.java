package com.example.ksandroidplayerdemo.Fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.bumptech.glide.request.transition.Transition;
import com.example.ksandroidplayerdemo.bean.Item;
import com.example.ksandroidplayerdemo.utils.Utils;
import com.example.ksandroidplayerdemo.R;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


import javax.security.auth.Subject;


public class CourseFragment extends Fragment implements View.OnClickListener{

    private List<Item> SubjectList = new ArrayList<>();
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
        toadd_SubjectList.add(new Item("语文"));
        toadd_SubjectList.add(new Item("数学"));
        toadd_SubjectList.add(new Item("英语"));
        toadd_SubjectList.add(new Item("物理"));
        toadd_SubjectList.add(new Item("化学"));
        toadd_SubjectList.add(new Item("生物"));
        toadd_SubjectList.add(new Item("政治"));
        toadd_SubjectList.add(new Item("历史"));
        toadd_SubjectList.add(new Item("地理"));

        total_SubjectList.add(new Item("语文"));
        total_SubjectList.add(new Item("数学"));
        total_SubjectList.add(new Item("英语"));
        total_SubjectList.add(new Item("物理"));
        total_SubjectList.add(new Item("化学"));
        total_SubjectList.add(new Item("生物"));
        total_SubjectList.add(new Item("政治"));
        total_SubjectList.add(new Item("历史"));
        total_SubjectList.add(new Item("地理"));

        radapter = new Recycler1(SubjectList);

        recyclerView = (RecyclerView) view.findViewById(R.id.viewpage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(radapter);
        setListener(view);
        return view;
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


    public class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {

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
            holder.Name.setOnClickListener(new View.OnClickListener() {//对加载的子项注册监听事件
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    String sub=mItemList.get(position).getName();


                    //按钮事件
                    Item item =new Item(sub);
                    SubFragments=new ArrayList<>();
                    Iterator<Item> iterator = total_SubjectList.iterator();
                    List<Item> lst1=new ArrayList<>();
                    List<Item> lst2=new ArrayList<>();
                    while (iterator.hasNext()){
                        Item next = iterator.next();
                        String name = next.getName();
                        if ((isIn(name,SubjectList)&&!name.equals(sub))||(isIn(name,toadd_SubjectList)&&name.equals(sub))){
                            lst1.add(new Item(name));
                            SubFragments.add(new SubjectFragment(name));
                        }
                        else{
                            lst2.add(new Item(name));
                        }
                    }
                    SubjectList= lst1;
                    toadd_SubjectList=lst2;
                    SubViewPager.setAdapter(new MyFragmentAdapter(getFragmentManager()));
                    recyclerView.setAdapter(new Recycler1(SubjectList));
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
            holder.Name.setOnClickListener(new View.OnClickListener() {//对加载的子项注册监听事件
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    String sub=mItemList.get(position).getName();
                    //按钮事件

                    SubViewPager.setCurrentItem(position);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Item item = mItemList.get(position);
            holder.Name.setText(item.getName());
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
