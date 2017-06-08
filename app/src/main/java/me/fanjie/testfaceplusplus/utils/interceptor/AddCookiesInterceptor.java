package me.fanjie.testfaceplusplus.utils.interceptor;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 获取保存cookie信息的拦截器
 * Created by dell on 2017/2/24.
 */

public class AddCookiesInterceptor implements Interceptor {
    private static final String COOKIE_PREF = "cookies_prefs";
    private Context mContext;

    public AddCookiesInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        String cookie = getCookie(request.url().toString(), request.url().host());
        if (!TextUtils.isEmpty(cookie)) {
            builder.addHeader("Cookie", cookie);
        }
        return chain.proceed(builder.build());
    }

    private String getCookie(String url, String domain) {
        SharedPreferences sp = mContext.getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
//        先取domain cookie 看看
        if (!TextUtils.isEmpty(domain)&&sp.contains(domain) && !TextUtils.isEmpty(sp.getString(domain, ""))) {

            return sp.getString(domain, "");
        }
        if (!TextUtils.isEmpty(url)&&sp.contains(url)&&!TextUtils.isEmpty(sp.getString(url,""))) {

            return sp.getString(url, "");
        }


        return null;
    }
}
