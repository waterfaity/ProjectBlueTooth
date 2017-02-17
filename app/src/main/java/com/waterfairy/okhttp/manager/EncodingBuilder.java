package com.waterfairy.okhttp.manager;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

/**
 * Created by water_fairy on 2016/8/5.
 */
public class EncodingBuilder {


    public StringBuffer content = new StringBuffer();

    private String encodeType;

    public EncodingBuilder(String encodeType) {
        this.encodeType = encodeType;

    }

    public EncodingBuilder add(String name, String value) {
        if (content.length() > 0) {
            content.append("&");
        }
        content.append(name + "=" + value);
        return this;
    }


    public RequestBody build() {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded;charset=" + encodeType);
        return RequestBody.create(mediaType, content.toString());
    }

}
