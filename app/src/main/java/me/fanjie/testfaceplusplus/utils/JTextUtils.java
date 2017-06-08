package me.fanjie.testfaceplusplus.utils;

import android.text.TextUtils;
import android.widget.TextView;

/**
 * Created by dell on 2017/3/8.
 */

public class JTextUtils {
    public static boolean isEmpty(String ... args){
        for (String str : args){
            if(TextUtils.isEmpty(str)){
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(TextView... args){
        for (TextView tv : args){
            if(TextUtils.isEmpty(tv.getText().toString())){
                return true;
            }
        }
        return false;
    }
}
