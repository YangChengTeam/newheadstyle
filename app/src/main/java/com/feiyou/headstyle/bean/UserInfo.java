package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2019/1/25.
 */
public class UserInfo {
    private String id;
    private String nickname;
    private String openid;
    private String sex;
    private String userimg;

    private String addr;
    private String birthday;
    private String intro;
    @SerializedName("image_wall")
    private String[] imageWall;
    private String phone;
    private String qq;
    private String weixin;

    private String sig;

    @SerializedName("collect_num")
    private int collectNum;

    @SerializedName("guan_num")
    private int guanNum;

    @SerializedName("fen_num")
    private int fenNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String[] getImageWall() {
        return imageWall;
    }

    public void setImageWall(String[] imageWall) {
        this.imageWall = imageWall;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public int getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public int getGuanNum() {
        return guanNum;
    }

    public void setGuanNum(int guanNum) {
        this.guanNum = guanNum;
    }

    public int getFenNum() {
        return fenNum;
    }

    public void setFenNum(int fenNum) {
        this.fenNum = fenNum;
    }
}
