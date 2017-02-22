package com.waterfairy.tool.rxjava.rxjava_retrofit.bean;

/**
 * Created by water_fairy on 2017/2/22.
 */

public class UpdateBean {

    /**
     * update : true
     * link : http://116.228.56.198:56001/evaluation-1.5.apk
     * compatible_version : 1.0
     * text : 1.5版本更新
     */

    private boolean update;
    private String link;
    private String compatible_version;
    private String text;

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCompatible_version() {
        return compatible_version;
    }

    public void setCompatible_version(String compatible_version) {
        this.compatible_version = compatible_version;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
