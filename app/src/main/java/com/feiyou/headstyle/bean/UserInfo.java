package com.feiyou.headstyle.bean;

import com.feiyou.headstyle.R;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import butterknife.BindView;

/**
 * Created by myflying on 2019/1/25.
 */
public class UserInfo {
    private String id;
    private String nickname;
    private String openid;
    private int sex;
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

    private String txopenid;

    @SerializedName("collect_num")
    private int collectNum;

    @SerializedName("guan_num")
    private int guanNum;

    @SerializedName("fen_num")
    private int fenNum;

    private boolean isFollow;

    @SerializedName("is_all_guan")
    private int isAllGuan;

    @SerializedName("type")
    private int loginType;

    private int age;

    private String star;

    @SerializedName("message_list")
    private List<NoteInfo> noteList;

    @SerializedName("sex_can_change")
    private int sexCanChange;

    @SerializedName("comment_num")
    private int commentNum;//评论消息数

    @SerializedName("aite_num")
    private int aiteNum;//艾特消息数

    @SerializedName("notice_num")
    private int noticeNum;//通知消息数

    @SerializedName("my_notice_num")
    private int myTotalNum;//我的消息总数

    @SerializedName("follow_num")
    private int followNum;

    @SerializedName("follower_num")
    private int followerNum;

    private String background;

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


    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
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

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public int getIsAllGuan() {
        return isAllGuan;
    }

    public void setIsAllGuan(int isAllGuan) {
        this.isAllGuan = isAllGuan;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public List<NoteInfo> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<NoteInfo> noteList) {
        this.noteList = noteList;
    }

    public int getSexCanChange() {
        return sexCanChange;
    }

    public void setSexCanChange(int sexCanChange) {
        this.sexCanChange = sexCanChange;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getAiteNum() {
        return aiteNum;
    }

    public void setAiteNum(int aiteNum) {
        this.aiteNum = aiteNum;
    }

    public int getNoticeNum() {
        return noticeNum;
    }

    public void setNoticeNum(int noticeNum) {
        this.noticeNum = noticeNum;
    }

    public int getMyTotalNum() {
        return myTotalNum;
    }

    public void setMyTotalNum(int myTotalNum) {
        this.myTotalNum = myTotalNum;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public int getFollowerNum() {
        return followerNum;
    }

    public void setFollowerNum(int followerNum) {
        this.followerNum = followerNum;
    }

    public String getTxopenid() {
        return txopenid;
    }

    public void setTxopenid(String txopenid) {
        this.txopenid = txopenid;
    }
}
