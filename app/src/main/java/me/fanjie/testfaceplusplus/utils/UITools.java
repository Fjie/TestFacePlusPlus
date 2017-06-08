package me.fanjie.testfaceplusplus.utils;

import android.text.TextUtils;
import android.widget.Toast;

import me.fanjie.testfaceplusplus.base.App;


/**
 * Created by dell on 2017/3/13.
 */

public class UITools {
    public static void showToast(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(App.getInstance(), msg, Toast.LENGTH_SHORT).show();
        }
    }

}
