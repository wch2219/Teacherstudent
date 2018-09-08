package com.jiaoshizige.teacherexam.model;

/**
 * Created by Administrator on 2018/7/16.
 */

public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
