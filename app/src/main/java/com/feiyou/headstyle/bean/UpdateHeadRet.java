package com.feiyou.headstyle.bean;

/**
 * Created by myflying on 2018/11/20.
 */
public class UpdateHeadRet extends ResultInfo {
    private UpdateHeadWrapper data;

    public UpdateHeadWrapper getData() {
        return data;
    }

    public void setData(UpdateHeadWrapper data) {
        this.data = data;
    }

    public class UpdateHeadWrapper {
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
