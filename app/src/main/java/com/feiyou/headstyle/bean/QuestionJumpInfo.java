package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2019/2/20.
 */
public class QuestionJumpInfo {

    @SerializedName("jump_answer")
    private String[] jumpAnswer;

    @SerializedName("jump_question")
    private String[] jumpQuestion;

    @SerializedName("jump_type")
    private int jumpType;

    public String[] getJumpAnswer() {
        return jumpAnswer;
    }

    public void setJumpAnswer(String[] jumpAnswer) {
        this.jumpAnswer = jumpAnswer;
    }

    public String[] getJumpQuestion() {
        return jumpQuestion;
    }

    public void setJumpQuestion(String[] jumpQuestion) {
        this.jumpQuestion = jumpQuestion;
    }

    public int getJumpType() {
        return jumpType;
    }

    public void setJumpType(int jumpType) {
        this.jumpType = jumpType;
    }
}
