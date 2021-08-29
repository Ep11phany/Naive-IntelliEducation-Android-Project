package com.example.ksandroidplayerdemo.Fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.ksandroidplayerdemo.MainActivity;
import com.example.ksandroidplayerdemo.R;
import com.example.ksandroidplayerdemo.Fragment.SubjectFragment;
import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ksandroidplayerdemo.Item;



public class CourseFragment extends Fragment implements View.OnClickListener{

    private List<Item> SubjectList = new ArrayList<>();
    private TextView plus_btn;
    private RecyclerView recyclerView;
    private View view;
    private Recycler adapter;
    private ViewPager SubViewPager;
    List<Fragment>SubFragments=new ArrayList<Fragment>();
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

        @Override
        public int getCount() {
            return SubFragments.size();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_course, container, false);
        plus_btn=(TextView)view.findViewById(R.id.plus_btn);
        recyclerView = (RecyclerView) view.findViewById(R.id.viewpage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Recycler(SubjectList);
        recyclerView.setAdapter(adapter);
        plusSubjects("语文");
        return view;
    }

    private void plusSubjects(String sub){
        Item item =new Item(sub);
        SubFragments.add(new SubjectFragment(sub));
        SubViewPager = (ViewPager) view.findViewById(R.id.subpage);
        SubViewPager.setAdapter(new MyFragmentAdapter(getFragmentManager()));
        SubjectList.add(item);
        Recycler adapter = new Recycler(SubjectList);
        recyclerView.setAdapter(adapter);
        setListener(view);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plus_btn:
                plusSubjects("数学");
                break;
        }
    }


    private void setListener(View v) {
        //SubjectList.get(i).setOnClickListener(this);
        recyclerView.setOnClickListener(this);
        plus_btn.setOnClickListener(this);
    }


    public class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {

        private List<Item> mItemList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView Name;
            public ViewHolder(View view) {
                super(view);
                Name = (TextView) view.findViewById(R.id.Subject);
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
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    Item item = mItemList.get(position);
                    SubViewPager.setCurrentItem(position);
                    //按钮事件
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
}
