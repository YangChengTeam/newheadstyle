package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2019/2/20.
 */
public class TestDetailInfo {

    private List<List<String>> answer;

    private List<String> question;

    private List<QuestionJumpInfo> jump;

    public List<List<String>> getAnswer() {
        return answer;
    }

    public void setAnswer(List<List<String>> answer) {
        this.answer = answer;
    }

    public List<String> getQuestion() {
        return question;
    }

    public void setQuestion(List<String> question) {
        this.question = question;
    }

    public List<QuestionJumpInfo> getJump() {
        return jump;
    }

    public void setJump(List<QuestionJumpInfo> jump) {
        this.jump = jump;
    }
}
