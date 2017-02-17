package com.waterfairy.okhttp.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.waterfairy.okhttp.helper.ProgressHelper;
import com.waterfairy.okhttp.listener.ProgressListener;
import com.waterfairy.okhttp.progress.ProgressRequestBody;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;

import com.waterfairy.okhttp.callback.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by water_fairy on 2016/1/13.
 * version 1.0
 * 该该框架修改子网上资源
 * 主体部分   采取 http://blog.csdn.net/lmj623565791/article/details/47911083；
 * 上/下载进度部分 采取 https://github.com/lizhangqu/CoreProgress
 * <p>
 * 基本请求用get post
 * 特殊的使用request
 */

public class OkHttpManager {
    private static OkHttpManager mInstance;
    private OkHttpClient mOkHttpClient;
    public static Handler mHandler;

    public static final String POST = "post";
    public static final String PATCH = "patch";
    public static final String PUT = "put";
    public static final String DEL = "del";
    public static final String GET = "get";

    public OkHttpManager() {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpClient getOkHttpClient() {
        return initInstance().mOkHttpClient;
    }

    public static OkHttpManager initInstance() {
        if (mInstance == null) {
            synchronized (OkHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpManager();
                    mInstance.getOkHttpClient().setConnectTimeout(5000, TimeUnit.MILLISECONDS);
                }
            }
        }
        return mInstance;
    }

    //get请求

    /**
     * @param url      地址
     * @param callback 回调
     */
    public void get(String url, Callback callback) {
        get(url, new Param[]{}, callback);
    }

    /**
     * @param url      地址
     * @param params   参数
     * @param callback 回调
     */
    public void get(String url, Param[] params, Callback callback) {
        request(GET, false, null, url, params, null, null, null, null, null, callback);
    }

    /**
     * @param url      地址
     * @param params   参数
     * @param headers  头部
     * @param callback 回调
     */
    public void get(String url, Param[] params, Param[] headers, Callback callback) {
        request(GET, false, null, url, params, null, null, null, null, headers, callback);
    }

    /**
     * @param url      地址
     * @param params   参数
     * @param callback 回调
     */
    public void post(String url, Param[] params, Callback callback) {
        request(POST, false, null, url, params, null, null, null, null, null, callback);
    }

    /**
     * @param encodeType 编码
     * @param url        地址
     * @param params     参数
     * @param callback   回调
     */
    public void post(String encodeType, String url, Param[] params, Callback callback) {
        request(POST, false, encodeType, url, params, null, null, null, null, null, callback);
    }

    //header
    public void post(String url, Param[] params, Param[] headers, Callback callback) {
        request(POST, false, null, url, params, null, null, null, null, headers, callback);
    }

    /**
     * @param url       地址
     * @param jsonName  json名字
     * @param jsonParam json参数
     * @param callback  回调
     */
    public void post(String url, String jsonName, String jsonParam, Callback callback) {

        if (jsonParam != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam);
            Headers headers = null;
            if (!TextUtils.isEmpty(jsonName)) {
                headers = Headers.of("Content-Disposition", "form-data; name=\"" + jsonName + "\"");
            }
            request(POST, url, requestBody, null, headers, callback);
        }
    }

    //图片显示(异步)
    public static void displayImg(ImageView vImg, String imgUrl, Bitmap bitmap) {
        initInstance().displayImg(vImg, imgUrl, bitmap, 0, null);
    }

    //图片显示(异步)
    public static void displayImg(ImageView vImg, String imgUrl, Bitmap bitmap, String savePath) {
        initInstance().displayImg(vImg, imgUrl, bitmap, 0, savePath);
    }

    //图片显示(异步)
    public static void displayImg(ImageView vImg, String imgUrl, int resId) {
        initInstance().displayImg(vImg, imgUrl, null, resId, null);
    }

    //图片显示(异步)
    public static void displayImg(ImageView vImg, String imgUrl, int resId, String savePath) {
        initInstance().displayImg(vImg, imgUrl, null, resId, savePath);
    }

    public void request(String method, boolean multi, String encodeType, String url, Param[] params, String jsonName, String jsonParam, String[] fileNames, File[] files, Param[] headers, final Callback callback) {


        RequestBody requestBody = null;


        //多类型
        MultipartBuilder multiPartBuilder = null;
        if (multi) {
            MediaType mediaType = MultipartBuilder.FORM;
            if (method.equals("put")) {
                mediaType = MultipartBuilder.PARALLEL;
            }
            new MultipartBuilder().type(mediaType);
            multiPartBuilder = multiPartBuilder.type(mediaType);
            //参数 -->多类型
            if (params != null) {
                for (Param param : params) {
                    Headers header = Headers.of("Content-Disposition", "form-data;name=\"" + param.getName() + "\"");
                    RequestBody requestBodyTemp = RequestBody.create(null, param.getValue());
                    multiPartBuilder.addPart(header, requestBodyTemp);
                }
            }
            //文件  -->多类型
            if (files != null) {
                RequestBody fileBody = null;
                int i = 0;
                for (File file : files) {
                    fileBody = RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
                    String fileName = "file";
                    if (fileNames != null) {
                        fileName = fileNames[i];
                    }
                    multiPartBuilder.addPart(
                            Headers.of(
                                    "Content-Disposition",
                                    "form-data; name=\"" + fileName + "\"; filename=\"" + file.getName() + "\""),
                            fileBody);
                    i++;
                }
            }
            //json  -->多类型

            //json
            Headers jsonHeader = null;
            RequestBody jsonRequestBody = null;
            //json请求
            if (jsonParam != null) {
                jsonRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam);
                if (!TextUtils.isEmpty(jsonName)) {
                    jsonHeader = Headers.of("Content-Disposition", "form-data; name=\"" + jsonName + "\"");
                    multiPartBuilder.addPart(jsonHeader, jsonRequestBody);
                } else {
                    multiPartBuilder.addPart(jsonRequestBody);
                }
            }
            requestBody = multiPartBuilder.build();
        } else {
            //单类型 ->编码(默认/特定)
            if (params.length == 0) {
                requestBody = null;
            } else {
                requestBody = paramToBuilder(params, encodeType);
            }
        }
        request(method, url, requestBody, headers, null, callback);
    }


    /**
     * @param method      方法
     * @param url         地址
     * @param requestBody 请求体
     * @param headers     头部1
     * @param header      头部2(json)
     * @param callback    回调
     */
    public void request(String method, String url, RequestBody requestBody, Param[] headers, Headers header, final Callback callback) {
        Request.Builder requestBuilder = new Request.Builder();
        //header
        if (headers != null) {
            for (int i = 0; i < headers.length; i++) {
                Param param = headers[i];
                requestBuilder.addHeader(param.getName(), param.getValue());
            }
        }
        if (header != null) {
            requestBuilder.headers(header);
        }
        ProgressRequestBody progressRequestBody = null;
        if (requestBody != null) {
            //上传进度
            progressRequestBody = ProgressHelper.addProgressRequestListener(requestBody, new ProgressListener() {
                @Override
                public void onProgress(long currentBytes, long contentLength, boolean done) {
                    callback.onProgress(currentBytes, contentLength, done);
                }
            });
        }


        switch (method) {
            case POST:
                requestBuilder.post(progressRequestBody);
                break;
            case PUT:
                requestBuilder.put(progressRequestBody);
                break;
            case DEL:
                requestBuilder.delete(progressRequestBody);
                break;
            case PATCH:
                requestBuilder.patch(progressRequestBody);
                break;
        }
        Request request = requestBuilder.url(url).build();
        mOkHttpClient.newCall(request).enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callback.onFailure(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                callback.onResponse(response);
            }
        });
    }

    /**
     * @param params     参数
     * @param encodeType 编码
     * @return
     */
    public static RequestBody paramToBuilder(Param[] params, String encodeType) {
        if (TextUtils.isEmpty(encodeType)) {
            FormEncodingBuilder builder = new FormEncodingBuilder();
            if (params != null) {
                for (Param param : params) {
                    builder.add(param.getName(), param.getValue());
                }
            }
            return builder.build();
        } else {
            EncodingBuilder encodingBuilder = new EncodingBuilder(encodeType);
            if (params != null) {
                for (Param param : params) {
                    encodingBuilder.add(param.getName(), param.getValue());
                }
            }
            return encodingBuilder.build();
        }
    }

    //下载
    public void downLoadFile(String url, final String path, final Callback callback) {
        final Request request = new Request.Builder().url(url).build();
        OkHttpClient client = ProgressHelper.addProgressResponseListener(mOkHttpClient, new ProgressListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength, boolean done) {
                callback.onProgress(currentBytes, contentLength, done);
            }
        });
        Call call = client.newCall(request);
        call.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callback.onFailure(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                callback.onResponse(response);
                try {
                    if (path == null || TextUtils.isEmpty(path)) {
                        return;
                    }
                    byte[] bytes = response.body().bytes();
                    if (bytes.length == 0) {
                        return;
                    }
                    File file = new File(path);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    OutputStream fos = new FileOutputStream(file);
                    fos.write(bytes);
                    fos.flush();
                    fos.close();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void displayImg(final ImageView vImg, String imgUrl, final Bitmap bitmap, int resId, final String savePath) {
        //默认 显示图片
        if (bitmap != null) {
            vImg.setImageBitmap(bitmap);
        }
        if (resId != 0) {
            vImg.setImageResource(resId);
        }


        if (TextUtils.isEmpty(imgUrl)) {
            return;
        }

        if (savePath != null) {
            File file = new File(savePath);
            if (file.exists()) {
                try {
                    vImg.setImageBitmap(BitmapFactory.decodeFile(savePath));
                    return;
                } catch (Exception e) {

                }
            }
        }

        if (imgUrl == null) {
            return;
        }

        final Request build = new Request.Builder().url(imgUrl).build();
        Call call = mOkHttpClient.newCall(build);
        call.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.w("displayImg", "get img res error!");
            }

            @Override
            public void onResponse(Response response) throws IOException {

                byte[] bytes = response.body().bytes();
                final Bitmap bitmap = saveImg(bytes, savePath);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        vImg.setImageBitmap(bitmap);
                    }
                });

            }
        });
    }

    private Bitmap saveImg(byte[] bytes, String savePath) {
        if (savePath == null) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        try {
            File file = new File(savePath);
            if (!file.exists()) {
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bytes);
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return BitmapFactory.decodeFile(savePath);
    }

    private String guessMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(fileName);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    public Param[] listToArray(List<Param> list) {
        if (list == null) return null;
        int size = list.size();
        Param[] params = new Param[size];
        for (int i = 0; i < size; i++) {
            params[i] = list.get(i);
        }
        return params;
    }

    public Param[] mapToArray(HashMap<String, String> hashMap) {
        if (hashMap == null) {
            return null;
        }
        int size = hashMap.size();
        Param[] param = new Param[size];
        Set set = hashMap.keySet();
        Iterator<String> iterator = set.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            String name = iterator.next() + "";
            String value = hashMap.get(name) + "";
            param[i] = new Param(name, value);
            i++;
        }
        return param;
    }

    public class Param {
        public Param(String name, String value) {
            this.name = name;
            this.value = value;
        }

        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
