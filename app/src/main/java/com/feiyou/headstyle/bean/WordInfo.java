package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2018/11/16.
 */
public class WordInfo {
    private String id;


    private String cid;

    private String word;

    @SerializedName("word_mean")
    private String wordMean;

    @SerializedName("word_img")
    private String wordImage;

    @SerializedName("mp3_url")
    private String wordMp3Url;

    @SerializedName("word_color")
    private String wordColor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordMean() {
        return wordMean;
    }

    public void setWordMean(String wordMean) {
        this.wordMean = wordMean;
    }

    public String getWordImage() {
        return wordImage;
    }

    public void setWordImage(String wordImage) {
        this.wordImage = wordImage;
    }

    public String getWordMp3Url() {
        return wordMp3Url;
    }

    public void setWordMp3Url(String wordMp3Url) {
        this.wordMp3Url = wordMp3Url;
    }

    public String getWordColor() {
        return wordColor;
    }

    public void setWordColor(String wordColor) {
        this.wordColor = wordColor;
    }
}
