package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by myflying on 2019/2/25.
 */
public class ForecastInfo {

    private List<ForecastSubInfo> list;

    @SerializedName("hui")
    private NumInfo numberInfo;

    private String uniqid;

    public List<ForecastSubInfo> getList() {
        return list;
    }

    public void setList(List<ForecastSubInfo> list) {
        this.list = list;
    }

    public NumInfo getNumberInfo() {
        return numberInfo;
    }

    public void setNumberInfo(NumInfo numberInfo) {
        this.numberInfo = numberInfo;
    }

    public String getUniqid() {
        return uniqid;
    }

    public void setUniqid(String uniqid) {
        this.uniqid = uniqid;
    }

    public class ForecastSubInfo {
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public class NumInfo {
        private int zhzs;
        private int aqzs;
        private int syzs;
        private int cfzs;
        private String spxz;
        private String xyys;
        private String jkzs;
        private String xysz;
        private String desc;

        public int getZhzs() {
            return zhzs;
        }

        public void setZhzs(int zhzs) {
            this.zhzs = zhzs;
        }

        public int getAqzs() {
            return aqzs;
        }

        public void setAqzs(int aqzs) {
            this.aqzs = aqzs;
        }

        public int getSyzs() {
            return syzs;
        }

        public void setSyzs(int syzs) {
            this.syzs = syzs;
        }

        public int getCfzs() {
            return cfzs;
        }

        public void setCfzs(int cfzs) {
            this.cfzs = cfzs;
        }

        public String getSpxz() {
            return spxz;
        }

        public void setSpxz(String spxz) {
            this.spxz = spxz;
        }

        public String getXyys() {
            return xyys;
        }

        public void setXyys(String xyys) {
            this.xyys = xyys;
        }

        public String getJkzs() {
            return jkzs;
        }

        public void setJkzs(String jkzs) {
            this.jkzs = jkzs;
        }

        public String getXysz() {
            return xysz;
        }

        public void setXysz(String xysz) {
            this.xysz = xysz;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

}
