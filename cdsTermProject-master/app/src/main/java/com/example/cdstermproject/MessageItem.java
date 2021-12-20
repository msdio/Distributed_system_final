package com.example.cdstermproject;

public class MessageItem {

    String name;
    String message;
    String time;
    String profileUrl;

    public MessageItem() {
        // 리얼타임 데이터베이스에서 객체를 읽어올 때, 비어있는 생성자가 필요하다.
        // iOS와 읽는 법이 많이 다르다 - 상당히 불편하다.
    }

    public MessageItem(String name, String message, String time, String pofileUrl) {
        this.name = name;
        this.message = message;
        this.time = time;
        this.profileUrl = pofileUrl;
    }

    //Getter & Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPofileUrl() {
        return profileUrl;
    }

    public void setPofileUrl(String pofileUrl) {
        this.profileUrl = pofileUrl;
    }
}
