package com.waterfairy.okhttp.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.waterfairy.okhttp.callback.Callback;
import com.waterfairy.okhttp.helper.ProgressHelper;
import com.waterfairy.okhttp.listener.ProgressListener;
import com.waterfairy.okhttp.progress.ProgressRequestBody;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
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
 * Created by water_fairy on 2016/1/11.
 * version 1.0
 * 该该框架修改子网上资源
 * 主体部分   采取 http://blog.csdn.net/lmj623565791/article/details/47911083；
 * 上/下载进度部分 采取 https://github.com/lizhangqu/CoreProgress
 */
public class OkHttpManager1 {

    private static OkHttpManager1 mInstance;
    private OkHttpClient mOkHttpClient;
    public static Handler mHandler;

    //    public static final String GET = "get";
    public static final String POST = "post";
    public static final String PATCH = "patch";
    public static final String PUT = "put";
    public static final String DEL = "del";

    public OkHttpManager1() {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpClient getOkHttpClient() {
        return initInstance().mOkHttpClient;
    }

    public static OkHttpManager1 initInstance() {
        if (mInstance == null) {
            synchronized (OkHttpManager1.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpManager1();
                    mInstance.getOkHttpClient().setConnectTimeout(5000, TimeUnit.MILLISECONDS);
                }
            }
        }
        return mInstance;
    }

//    //同步get请求
//    public Response getAsyn(String url) throws IOException {
//        return initInstance()._getAsyn(url);
//    }

    //异步get请求
    public void get(String url, Callback callback) {
        initInstance()._getAsyn(url, null, null, callback);
    }

    //异步get请求 参数params[]
    public void get(String url, Param[] params, Callback callback) {
        initInstance()._getAsyn(url, params, null, callback);
    }

    //异步get请求 参数list
    public void get(String url, List<Param> list, Callback callback) {
        initInstance()._getAsyn(url, listToParams(list), null, callback);
    }


    //异步get 请求 添加 header
    public void get(String url, HashMap hashMap, Callback callback) {
        initInstance()._getAsyn(url, null, mapToParams(hashMap), callback);
    }


//    //同步post请求
//    public Response postAsyn(String url, Param[] params) throws IOException {
//        return initInstance()._postAsyn(url, params);
//    }
//
//    //编码 request
//    public void post(String url,String  String param, Callback callback) {
////        Request request = new Request.Builder().url(url).post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"), param)).build();
////        Call call = mOkHttpClient.newCall(request);
////        call.enqueue(callback);
//
//    }

    //异步post请求
    public void post(String url, HashMap hashMap, Callback callback) {
        Param[] params = mapToParams(hashMap);
        initInstance()._postAsyn(POST, null, url, params, callback);
    }

    //异步post请求
    public void post(String url, String encodeType, HashMap hashMap, Callback callback) {
        Param[] params = mapToParams(hashMap);
        initInstance()._postAsyn(POST, encodeType, url, params, callback);
    }


    //异步 request
    public void request(String method, String url, HashMap hashMap, Callback callback) {

        Param[] params = mapToParams(hashMap);
        _postAsyn(method, null, url, params, callback);


    }

    //异步 request 编码
    public void request(String method, String encodeType, String url, HashMap hashMap, Callback callback) {

        Param[] params = mapToParams(hashMap);
        _postAsyn(method, encodeType, url, params, callback);
    }


    //异步 post json 请求
    public void post(String url, String jsonParam, Callback callback) {
        initInstance()._postAsyn(POST, url, jsonParam, "", callback);

    }

    //异步 post json 请求
    public void post(String url, String jsonParam, String jsonName, Callback callback) {
        initInstance()._postAsyn(POST, url, jsonParam, jsonName, callback);

    }

    //文件下载
    public void downLoadFile(String url, String path, Callback callback) {
        initInstance()._downLoadFile(url, path, callback);
    }

    //文件上传,无参
    public void uploadFile(String method, String url, File[] files, Callback callback) {
        initInstance()._uploadFIle(method, url, files, null, null, null, null, callback);

    }

    //文件上传,有参
    public void uploadFile(String method, String url, File[] files, Param[] params, Callback callback) {
        initInstance()._uploadFIle(method, url, files, null, params, null, null, callback);

    }

    //文件上传,有参 HashMap
    public void uploadFile(String method, String url, File[] files, HashMap hashMap, Callback callback) {

        initInstance()._uploadFIle(method, url, files, null, mapToParams(hashMap), null, null, callback);

    }

    //文件上传,有参 HashMap
    public void uploadFile(String method, String url, File[] files, List<Param> list, Callback callback) {

        initInstance()._uploadFIle(method, url, files, null, listToParams(list), null, null, callback);

    }

    //文件上传,json
    public void uploadFile(String method, String url, File[] files, String[] fileNames, String jsonParam, String jsonName, Callback callback) {
        initInstance()._uploadFIle(method, url, files, fileNames, null, jsonParam, jsonName, callback);

    }

    //图片显示(异步)
    public static void displayImg(ImageView vImg, String imgUrl, Bitmap bitmap) {
        initInstance()._displayImg(vImg, imgUrl, bitmap, 0, null);
    }

    //图片显示(异步)
    public static void displayImg(ImageView vImg, String imgUrl, Bitmap bitmap, String savePath) {
        initInstance()._displayImg(vImg, imgUrl, bitmap, 0, savePath);
    }

    //图片显示(异步)
    public static void displayImg(ImageView vImg, String imgUrl, int resId) {
        initInstance()._displayImg(vImg, imgUrl, null, resId, null);
    }

    //图片显示(异步)
    public static void displayImg(ImageView vImg, String imgUrl, int resId, String savePath) {
        initInstance()._displayImg(vImg, imgUrl, null, resId, savePath);
    }


    //del 请求
    public void del(String url, final Callback callback) {

        Request request = new Request.Builder()
                .delete()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new com.squareup.okhttp.Callback() {
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

    //json
    private void _postAsyn(String method, String url, String jsonParam, String jsonName, final Callback callback) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam);


//        if (TextUtils.isEmpty(jsonName)) {
//            builder.addPart(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam));
//        } else {
//
//            builder.addPart(,
//                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam));
//        }
        Request.Builder builder = new Request.Builder();
        if (!TextUtils.isEmpty(jsonName)) {
            builder.headers(Headers.of("Content-Disposition", "form-data; name=\"" + jsonName + "\""));
        }


        Request request = null;
        Request.Builder requestBuilder = builder.url(url);

        ProgressRequestBody progressRequestBody = ProgressHelper.addProgressRequestListener(requestBody, new ProgressListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength, boolean done) {
                callback.onProgress(currentBytes, contentLength, done);
            }
        });

        switch (method) {
            case POST:
                request = requestBuilder.post(progressRequestBody).build();
                break;
            case PUT:
                request = requestBuilder.put(progressRequestBody).build();
                break;
            case DEL:
                request = requestBuilder.delete(progressRequestBody).build();
                break;
            case PATCH:
                request = requestBuilder.patch(progressRequestBody).build();
                break;
        }


        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new com.squareup.okhttp.Callback() {
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
     * @param method    方法 post, , patch 不全,默认post ,后期添加
     * @param url       请求地址
     * @param files     文件参数 数组形式
     * @param fileNames 文件名参数 数组形式 为null 默认设置为 文件名
     * @param params    参数 数组形式
     * @param jsonParam json参数
     * @param jsonName  json参数名 默认没有
     * @param callback
     */
    private void _uploadFIle(String method, String url, File[] files, String[] fileNames, Param[] params, String jsonParam, String jsonName, final Callback callback) {
        MediaType mediaType = MultipartBuilder.FORM;
        if (method.equals("put")) {
            mediaType = MultipartBuilder.PARALLEL;
        }
        MultipartBuilder builder = new MultipartBuilder().type(mediaType);
        builder.type(mediaType);
        if (params != null) {
            for (Param param : params) {
                builder.addPart(
                        Headers.of("Content-Disposition", "form-data;name=\"" + param.getName() + "\""),
                        RequestBody.create(null, param.getValue()));
//                MediaType.parse("charset=utf-8")
            }
        }
        if (jsonParam != null) {
            if (TextUtils.isEmpty(jsonName)) {
                builder.addPart(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam));
            } else {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + jsonName + "\""),
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam));
            }
        }
        if (files != null) {
            RequestBody fileBody = null;
            int i = 0;
            for (File file : files) {
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
                String fileName = "file";
                if (fileNames != null) {
                    fileName = fileNames[i];
                }
                builder.addPart(
                        Headers.of(
                                "Content-Disposition",
//                                "form-data; name=\"file\"; filename=\"" + fileName + "\""),
                                "form-data; name=\"" + fileName + "\"; filename=\"" + file.getName() + "\""),
                        fileBody);
                i++;
//                        Headers.of(
//                                "Content-Disposition",
//                                "form-data; name=\"" + file + "\"; filename=\"" + file.getName() + "\""),
//                        fileBody);
            }
        }
        RequestBody requestBody = builder.build();


        Request request = null;
//        request.bu

        ProgressRequestBody progressRequestBody = ProgressHelper.addProgressRequestListener(requestBody, new ProgressListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength, boolean done) {
                callback.onProgress(currentBytes, contentLength, done);
            }
        });
        switch (method) {
            case POST:
                request = new Request.Builder().post(progressRequestBody).url(url).build();
                break;
            case PUT:
                request = new Request.Builder().put(progressRequestBody).url(url).build();
                break;
            case DEL:
                request = new Request.Builder().delete(progressRequestBody).url(url).build();
                break;
            case PATCH:
                request = new Request.Builder().patch(progressRequestBody).url(url).build();
                break;
        }


        Call call = mOkHttpClient.newCall(request);
        RequestBody body = request.body();
        try {
            body.contentLength();
            body.contentType();

        } catch (IOException e) {
            e.printStackTrace();
        }
        call.enqueue(new com.squareup.okhttp.Callback() {
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


    private String guessMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(fileName);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


//    private Response _postAsyn(String url, Param[] params) throws IOException {
//
//        Request build = new Request.Builder()
//                .url(url)
//                .post(paramToBuilder(params, null))
//                .build();
//        Call call = mOkHttpClient.newCall(build);
//        return call.execute();
//    }

    //post
    private void _postAsyn(String method, String encodeType, String url, Param[] params, final Callback callback) {

        Request build = null;
        RequestBody requestBody = null;
//        if (TextUtils.isEmpty(encodeType)) {
        requestBody = paramToBuilder(params, encodeType);
//        } else {
//            requestBody = paramToBuilder(params,encodeType).build();
//        }
        ProgressRequestBody progressRequestBody = ProgressHelper.addProgressRequestListener(requestBody, new ProgressListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength, boolean done) {
                callback.onProgress(currentBytes, contentLength, done);
            }
        });
//        progressRequestBody.contentType()=RequestBody.create(MediaType.parse(""),);

        switch (method) {
            case POST:
                build = new Request.Builder().url(url).post(progressRequestBody).build();
                break;
            case DEL:
                build = new Request.Builder().url(url).delete(progressRequestBody).build();
                break;
            case PUT:
                build = new Request.Builder().url(url).put(progressRequestBody).build();
                break;
            case PATCH:
                build = new Request.Builder().url(url).patch(progressRequestBody).build();
                break;
        }


        final Call call = mOkHttpClient.newCall(build);
        call.enqueue(new com.squareup.okhttp.Callback() {
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


    private void _downLoadFile(String url, final String path, final Callback callback) {
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
//                    File parentFile = file.getParentFile();
//                    if (!parentFile.exists()) {
//                        parentFile.mkdirs();
//                    }
//
//                    if (file.createNewFile()) {
//
//                    }
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

    //get
    private void _getAsyn(String url, Param[] params, Param[] headerParams, final Callback callback) {
        if (params != null) {
//            url += "";
            String temp = "?";
            for (int i = 0; i < params.length; i++) {
                Param param = params[i];
                temp = temp + param.name + "=" + param.value + "&";
            }
            url += temp.substring(0, temp.length() - 1);
        }


//        final Request request = new Request.Builder().url(url).build();

        Request.Builder builder = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK);

        if (headerParams != null) {
            for (int i = 0; i < headerParams.length; i++) {
                Param param = headerParams[i];
                builder.addHeader(param.getName(), param.getValue());
            }
        }
        final Request request = builder.url(url).build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new com.squareup.okhttp.Callback() {
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

//    private Response _getAsyn(String url) throws IOException {
//        final Request request = new Request.Builder().url(url).build();
//        Call call = mOkHttpClient.newCall(request);
//        return call.execute();
//    }

    private void _displayImg(final ImageView vImg, String imgUrl, final Bitmap bitmap, int resId, final String savePath) {
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

        if (imgUrl==null){
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

    private Param[] listToParams(List<Param> list) {

        int size = list.size();
        Param[] params = new Param[size];
        for (int i = 0; i < size; i++) {
            params[i] = list.get(i);
        }
        return params;

    }

    private Param[] mapToParams(HashMap hashMap) {

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
