package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2019/2/21.
 */
public class TestResultInfo {

    private String image;

    @SerializedName("image_nocode")
    private String imageNocode;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageNocode() {
        return imageNocode;
    }

    public void setImageNocode(String imageNocode) {
        this.imageNocode = imageNocode;
    }
}
