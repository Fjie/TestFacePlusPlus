package me.fanjie.testfaceplusplus.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by dell on 2017/3/7.
 */

public class Json {
    private static Json json;

    private Gson gson;

    public static Json getInstance() {
        if (json == null) {
            json = new Json();
        }
        return json;
    }

    private Json() {
        gson = new Gson();
    }

    public <T> T toEntity(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public <T> T toEntity(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public String toJson(Object object) {
        return gson.toJson(object);
    }

}
