package com.waterfairy.tool.rxjava.rxjava_retrofit.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by water_fairy on 2017/2/22.
 */

public class BaseResponseArray<T> {

    @SerializedName("data")
    public List<T> managerList;

    @SerializedName("code")
    public int code;

    @SerializedName("message")
    public String message;
}
