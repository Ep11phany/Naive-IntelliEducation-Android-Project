package com.example.ksandroidplayerdemo;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface OnItemClickListener{//也可以不在这个activity或者是fragment中来声明接口，可以在项目中单独创建一个interface，就改成static就OK
    //参数（父组件，当前单击的View,单击的View的位置，数据）
    void onItemClick(RecyclerView parent, View view, int position);
    // void onItemLongClick(View view);类似，我这里没用就不写了

}

