package me.fanjie.testfaceplusplus.core;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static me.fanjie.testfaceplusplus.core.C.IS_DEBUG;

/**
 * Created by dell on 2017/3/7.
 */

public class Net {

    private static Net net;

    private Retrofit retrofit;

    public static Net getInstance() {
        if (net == null) {
            net = new Net();
        }
        return new Net();
    }

    private Net() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (IS_DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(C.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }
}
