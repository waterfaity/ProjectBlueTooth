package com.waterfairy.okhttp.callback;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by water_fairy on 2016/1/14.
 */
public interface Callback {
    void onResponse(Response response) throws IOException;

    void onProgress(long currentBytes, long contentLength, boolean done);

    void onFailure(Request request, IOException e);
}
