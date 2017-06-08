package me.fanjie.testfaceplusplus.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import me.fanjie.testfaceplusplus.R;

import static me.fanjie.testfaceplusplus.utils.JTextUtils.isEmpty;


/**
 * Created by dell on 2017/3/7.
 */

public class BaseActivity extends AppCompatActivity {

    private AlertDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void showLoading() {
        showLoading("");
    }

    public void showLoading(String msg) {
        if (loadingDialog == null) {
            loadingDialog = new AlertDialog.Builder(this)
                    .setView(R.layout.dialog_loading)
                    .setCancelable(false)
                    .create();
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        if (!isEmpty(msg)) {
            loadingDialog.setMessage(msg);
        }
        loadingDialog.show();
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }


}
