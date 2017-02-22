package com.waterfairy.tool.rxjava.rxjava_retrofit.manger;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by water_fairy on 2017/2/22.
 */

public class HeaderManager {

    public static Headers createHeader(String name) {
        return Headers.of(
                "Content-Disposition",
                "form-data; name=\"" + name + "\"");

    }

    public static Headers createFileNameHeader(String name, String fileName) {
        return Headers.of(
                "Content-Disposition",
                "form-data; name=\"" + name + "\"; filename=\"" + fileName + "\"");
    }

}
