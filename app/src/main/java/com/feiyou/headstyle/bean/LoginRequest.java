package com.feiyou.headstyle.bean;

/**
 * Created by myflying on 2019/1/25.
 */
public class LoginRequest {

    private String type;
    private String openid;
    private String imeil;
    private String sex;
    private String qq;
    private String weixin;
    private String nickname;
    private String userimg;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getImeil() {
        return imeil;
    }

    public void setImeil(String imeil) {
        this.imeil = imeil;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }
}
