package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2018/11/20.
 */
public class FeedBackRet extends ResultInfo {
    private FeedBackWrapper data;

    public FeedBackWrapper getData() {
        return data;
    }

    public void setData(FeedBackWrapper data) {
        this.data = data;
    }

    public class FeedBackWrapper {
        @SerializedName("user_id")
        private String userId;
        private String content;
        private String phone;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
