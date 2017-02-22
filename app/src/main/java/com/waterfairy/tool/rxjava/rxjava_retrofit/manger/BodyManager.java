package com.waterfairy.tool.rxjava.rxjava_retrofit.manger;

import android.text.TextUtils;


import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by water_fairy on 2017/2/22.
 */

public class BodyManager {

    public static RequestBody createParamsBody(String param) {
        return RequestBody.create(null, param);
    }


    public static RequestBody createJsonBody(String jsonParam) {
        RequestBody jsonRequestBody = null;
        if (jsonParam != null) {
            jsonRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam);
        }
        return jsonRequestBody;
    }



    public static RequestBody createFileBody(File file) {
        return RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
    }

    /**
     * @param fileName
     * @return
     */
    private static String guessMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(fileName);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
