package me.fanjie.testfaceplusplus.utils;

import android.util.Log;

/**
 * Created by dell on 2017/2/8. 基础日志类
 */

public class JLog {
    private static final String TAG = "MY_TAG:";

    public static void d(Object obj) {
        Log.d(TAG + Thread.currentThread().getStackTrace()[3].toString(), String.valueOf(obj));
    }

    public static void e(Object obj) {
        Log.e(TAG + Thread.currentThread().getStackTrace()[3].toString(), String.valueOf(obj));
    }

    public static void v(Object obj) {
        Log.v(TAG + Thread.currentThread().getStackTrace()[3].toString(), String.valueOf(obj));
    }

    public static void w(Object obj) {
        Log.w(TAG + Thread.currentThread().getStackTrace()[3].toString(), String.valueOf(obj));
    }

    public static void i(Object obj) {
        Log.i(TAG + Thread.currentThread().getStackTrace()[3].toString(), String.valueOf(obj));
    }

}
