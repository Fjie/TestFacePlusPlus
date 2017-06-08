package me.fanjie.testfaceplusplus.core;

import android.accounts.NetworkErrorException;

import java.net.SocketTimeoutException;

import me.fanjie.testfaceplusplus.base.BaseActivity;
import me.fanjie.testfaceplusplus.utils.JLog;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static me.fanjie.testfaceplusplus.core.C.IS_DEBUG;
import static me.fanjie.testfaceplusplus.utils.UITools.showToast;

/**
 * Created by dell on 2017/3/7.
 */

public abstract class Callback<T> implements retrofit2.Callback<T> {

    private BaseActivity baseActivity;

    public Callback(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public Callback() {
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        dismissContextLoading();
        T body = response.body();
        if(IS_DEBUG) {
            JLog.v(call.request().url());
            RequestBody requestBody = call.request().body();
            if (requestBody instanceof FormBody) {
                FormBody formBody = (FormBody) requestBody;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < formBody.size(); i++) {
                    sb.append(formBody.encodedName(i)).append("=").append(formBody.encodedValue(i)).append(",");
                }
                sb.delete(sb.length() - 1, sb.length());
                JLog.v(sb);
            }
        }
        int code = response.code();
        if(code == 200) {
            if (body != null) {
                JLog.v(body);
                getBody(body);
            }else {
                onFailure(call,new RuntimeException("数据解析异常"));
            }
        }else if (code == 404) {
            onFailure(call, new NetworkErrorException("请求失败404"));
        } else if (code == 500) {
            onFailure(call, new NetworkErrorException("服务器错误500"));
        } else {
            onFailure(call, new NetworkErrorException("网络错误" + code));
        }
    }

    public abstract void getBody(T body);

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        JLog.e(t);
        dismissContextLoading();
        if (t instanceof SocketTimeoutException) {
            showToast("网络超时");
        } else if (t instanceof NetworkErrorException){
            showToast(t.getMessage());
        }else if (t instanceof RuntimeException){
            showToast(t.getMessage());
        }
    }

    protected void dismissContextLoading() {
        if (baseActivity != null) {
            baseActivity.dismissLoading();
        }
    }
}
