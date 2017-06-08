package me.fanjie.testfaceplusplus.utils.interceptor;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import me.fanjie.testfaceplusplus.utils.JLog;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 保存获取cookie的拦截器
 * Created by dell on 2017/2/24.
 */

public class SaveCookiesInterceptor implements Interceptor {
    private Context mContext;
    private static final String COOKIE_PREF = "cookies_prefs";

    public SaveCookiesInterceptor(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        //set-cookie可能为多个
        if (!response.headers("set-cookie").isEmpty()) {
            List<String> cookies = response.headers("set-cookie");
            String cookie = encodeCookie(cookies);
            JLog.i("intercept: cookie信息"+cookie);
            saveCookie(request.url().toString(),request.url().host(),cookie);
        }
        return response;
    }

    //整合cookie为唯一字符串
    private String encodeCookie(List<String> cookies) {
        StringBuilder sb = new StringBuilder();
        Set<String> set=new HashSet<>();
        for (String cookie : cookies) {
            String[] arr = cookie.split(";");
            for (String s : arr) {
                if(set.contains(s))continue;
                set.add(s);
            }
        }

        Iterator<String> ite = set.iterator();
        while (ite.hasNext()) {
            String cookie = ite.next();
            sb.append(cookie).append(";");
        }

        int last = sb.lastIndexOf(";");
        if (sb.length() - 1 == last) {
            sb.deleteCharAt(last);
        }

        return sb.toString();
    }

    //保存cookie到本地，这里我们分别为该url和host设置相同的cookie，其中host可选
    //这样能使得该cookie的应用范围更广
    private void saveCookie(String url,String domain,String cookies) {
        SharedPreferences sp = mContext.getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("url is null.");
        }else{
            editor.putString(url, cookies);
        }
        if (!TextUtils.isEmpty(domain)) {
            editor.putString(domain, cookies);
        }
            editor.apply();
    }

}
