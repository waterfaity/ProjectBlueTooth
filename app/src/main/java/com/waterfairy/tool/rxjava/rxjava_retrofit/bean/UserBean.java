package com.waterfairy.tool.rxjava.rxjava_retrofit.bean;

/**
 * Created by water_fairy on 2017/2/22.
 */

public class UserBean {
    private String nickName;
    private long userId;
    private String userName;
    private String sponsorLogo;
    private int firstLogin;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSponsorLogo() {
        return sponsorLogo;
    }

    public void setSponsorLogo(String sponsorLogo) {
        this.sponsorLogo = sponsorLogo;
    }

    public int getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(int firstLogin) {
        this.firstLogin = firstLogin;
    }
}
