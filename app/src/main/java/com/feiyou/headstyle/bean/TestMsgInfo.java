package com.feiyou.headstyle.bean;

import java.util.List;

public class TestMsgInfo {

    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    public static final int TYPE_RECEIVED_IMAGE = 2;
    private String content;
    private String imgUrl;
    private int type;

    private String codeImageUrl;

    private String resultImageUrl;

    private List<String> answer;

    private boolean clickable = true;

    public TestMsgInfo() {
    }

    public TestMsgInfo(String imgUrl,String content, int type) {
        this.imgUrl = imgUrl;
        this.content = content;
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public String getResultImageUrl() {
        return resultImageUrl;
    }

    public void setResultImageUrl(String resultImageUrl) {
        this.resultImageUrl = resultImageUrl;
    }

    public String getCodeImageUrl() {
        return codeImageUrl;
    }

    public void setCodeImageUrl(String codeImageUrl) {
        this.codeImageUrl = codeImageUrl;
    }
}
