package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2019/1/31.
 */
public class PhotoWallInfo {

    @SerializedName("image_wall")
    private String[] imageWall;

    public String[] getImageWall() {
        return imageWall;
    }

    public void setImageWall(String[] imageWall) {
        this.imageWall = imageWall;
    }
}
