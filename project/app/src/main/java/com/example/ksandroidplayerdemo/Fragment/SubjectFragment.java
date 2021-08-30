package com.example.ksandroidplayerdemo.Fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ksandroidplayerdemo.R;

public class SubjectFragment extends Fragment {
    public TextView Name;
    public View view;
    public String Subject;
    public SubjectFragment(String name) {
        Subject=name;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_subject, container, false);
        Name=(TextView)view.findViewById(R.id.subject);
        Name.setText(Subject);
        return view;
    }

}
