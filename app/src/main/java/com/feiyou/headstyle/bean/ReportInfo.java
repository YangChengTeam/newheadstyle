package com.feiyou.headstyle.bean;

/**
 * Created by myflying on 2019/3/14.
 */
public class ReportInfo {

    private String reportTypeName;

    private boolean isSelected;

    public ReportInfo(String name){
        this.reportTypeName = name;
    }

    public String getReportTypeName() {
        return reportTypeName;
    }

    public void setReportTypeName(String reportTypeName) {
        this.reportTypeName = reportTypeName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
