package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/20.
 */
public class StarPosterRet extends ResultInfo {
    private ResultImageWrapper data;

    public ResultImageWrapper getData() {
        return data;
    }

    public void setData(ResultImageWrapper data) {
        this.data = data;
    }

    public class ResultImageWrapper{
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
