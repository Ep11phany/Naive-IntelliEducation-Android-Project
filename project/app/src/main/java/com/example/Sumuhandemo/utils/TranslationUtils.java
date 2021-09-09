package com.example.Sumuhandemo.utils;


public class TranslationUtils {
    public static String E2C(String E){
        String sub=E;
        switch (E){
            case "chinese":
                sub="语文";
                break;
            case "math":
                sub="数学";
                break;
            case "english":
                sub="英语";
                break;
            case "physics":
                sub="物理";
                break;
            case "chemistry":
                sub="化学";
                break;
            case "biology":
                sub="生物";
                break;
            case "politics":
                sub="政治";
                break;
            case "history":
                sub="历史";
                break;
            case "geo":
                sub="地理";
                break;
        }
        return sub;
    }

    public static String C2E(String C){
        String sub=C;
        switch (C){
            case "语文":
                sub="chinese";
                break;
            case "数学":
                sub="math";
                break;
            case "英语":
                sub="english";
                break;
            case "物理":
                sub="physics";
                break;
            case "化学":
                sub="chemistry";
                break;
            case "生物":
                sub="biology";
                break;
            case "政治":
                sub="politics";
                break;
            case "历史":
                sub="history";
                break;
            case "地理":
                sub="geo";
                break;
        }
        return sub;
    }


}
