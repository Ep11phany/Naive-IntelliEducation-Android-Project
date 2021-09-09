package com.example.Sumuhandemo.utils;

import androidx.fragment.app.Fragment;

import java.util.LinkedHashMap;
import java.util.HashMap;

public class Utils {
    private static HashMap<Integer, Fragment> objMaps=new LinkedHashMap<Integer,Fragment>();

    public static void setFragmentForPosition(Fragment f,int position){
        objMaps.put(Integer.valueOf(position),f);
    }
    public static void removeFragment(int position){
        objMaps.remove(Integer.valueOf(position));
    }
    public static Fragment findViewFromFragment(int position){
        Fragment f= objMaps.get(Integer.valueOf(position));
        if(f!=null){
            return f;
        }
        return null;
    }


}
