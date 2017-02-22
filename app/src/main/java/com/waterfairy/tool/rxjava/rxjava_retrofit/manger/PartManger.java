package com.waterfairy.tool.rxjava.rxjava_retrofit.manger;

import android.text.TextUtils;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by water_fairy on 2017/2/22.
 */

public class PartManger {
    public static MultipartBody.Part createPart(String name, String param) {
        return MultipartBody.Part.create(HeaderManager.createHeader(name), BodyManager.createParamsBody(param));
    }
    public static MultipartBody.Part createJsonPart(String name, String jsonParam) {
        RequestBody jsonBody = BodyManager.createJsonBody(jsonParam);

        if (TextUtils.isEmpty(name)) {
            return MultipartBody.Part.create(jsonBody);
        } else {
            return MultipartBody.Part.create(HeaderManager.createHeader(name), jsonBody);
        }
    }

    public static MultipartBody.Part createPart(String name, File file) {
        RequestBody fileBody = BodyManager.createFileBody(file);
        if (TextUtils.isEmpty(name)) {
            return MultipartBody.Part.create(fileBody);
        } else {
            return MultipartBody.Part.create(HeaderManager.createFileNameHeader(name, file.getName()), fileBody);
        }
    }

}
