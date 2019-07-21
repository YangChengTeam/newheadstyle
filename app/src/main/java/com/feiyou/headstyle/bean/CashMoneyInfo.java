package com.feiyou.headstyle.bean;

public class CashMoneyInfo {

    private int stype;//提现方式(1:极速，2：普通)

    private int amount;//可提现金额(多个金额用英文逗号给开)

    private boolean isSelected;

    public int getStype() {
        return stype;
    }

    public void setStype(int stype) {
        this.stype = stype;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
