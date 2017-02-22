package com.waterfairy.tool.rxjava.rxjava_retrofit.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by water_fairy on 2017/2/22.
 */

public class BaseResponseObject<T> {

    @SerializedName("data")
    public T data;

    @SerializedName("code")
    public int code;

    @SerializedName("message")
    public String message;
}
