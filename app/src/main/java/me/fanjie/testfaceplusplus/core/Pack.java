package me.fanjie.testfaceplusplus.core;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2017/3/7. 包装
 */

public class Pack<T> {

    @SerializedName("c")
    private int stateCode;
    @SerializedName("d")
    private T data;
    @SerializedName("s")
    private String msg;

    public Pack() {
        stateCode = -99;
    }

    //    成功
    public boolean isSuccess() {
        return stateCode == 0;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String dataString(){
        return data == null ? "null": data.getClass().getSimpleName();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return dataString()+ ", stateCode='" + stateCode + '\'' + ", msg='" + msg + '\'' ;
    }
}
