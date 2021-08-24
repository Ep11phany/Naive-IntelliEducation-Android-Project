package com.example.ksandroidplayerdemo.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.example.ksandroidplayerdemo.R;
import com.example.ksandroidplayerdemo.Fragment.SubjectFragment;
import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.ksandroidplayerdemo.Recycler;
import com.example.ksandroidplayerdemo.Item;



public class CourseFragment extends Fragment implements View.OnClickListener{

    private List<Item> SubjectList = new ArrayList<>();
    private TextView plus_btn;
    public CourseFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_course, container, false);
        plus_btn=(TextView)view.findViewById(R.id.plus_btn);
        plusSubjects();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.viewpage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        Recycler adapter = new Recycler(SubjectList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void plusSubjects(){
        Item item =new Item("数学");
        SubjectList.add(item);

    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plus_btn:
                plusSubjects();
                break;
        }
    }


    private void setListener(View v) {
        for (int i = 0; i < SubjectList.size(); i++) {
           //SubjectList.get(i).setOnClickListener(this);
        }
        plus_btn.setOnClickListener(this);
    }

/*    public void addBottomTab(LinearLayout container, String bottomTitleArr){
            TextView childView =new TextView(container.getContext());// (TextView) View.inflate(container.getContext(), R.layout.subject_button, null);
            //给TextView添加文本
            childView.setText(bottomTitleArr);
            childView.setBackgroundColor(0x000000);
            childView.setTextColor(0x000000);
            //修改对应位置的图片.参数代表位于TextView的哪个方位。仅仅位于上方
            //childView.setWidth(100);
            //把两个底部tab平分秋色.使用paramas对象
            int w = 100;
            int h = LinearLayout.LayoutParams.WRAP_CONTENT;
            //创建params对象，并绘制具体的控件的宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w,h);
            //weight设置为1，才是真正的均分父容器宽度
            childView.setLayoutParams(params);

            params.weight = 1;
            container.addView(childView,params);
        }


    class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }*/



}
