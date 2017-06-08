package me.fanjie.testfaceplusplus.core;

import android.content.Context;
import android.content.SharedPreferences;

import me.fanjie.testfaceplusplus.base.App;


/**
 * Created by dell on 2017/3/7. 常量
 */

public class CoreData {

    private static CoreData core;

    private SharedPreferences pref;

    public static CoreData getInstance() {
        if (core == null) {
            core = new CoreData();
        }
        return core;
    }

    private CoreData() {
        pref = App.getInstance().getSharedPreferences(
                C.PrefKey.NAME, Context.MODE_PRIVATE);
    }


}
