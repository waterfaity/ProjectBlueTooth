package com.waterfairy.okhttp.callback;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by water_fairy on 2016/1/22.
 */
public abstract class ProgressCallback implements Callback {
    @Override
    public abstract void onResponse(Response response) throws IOException;

    @Override
    public abstract void onProgress(long currentBytes, long contentLength, boolean done);

    @Override
    public abstract void onFailure(Request request, IOException e);
}
