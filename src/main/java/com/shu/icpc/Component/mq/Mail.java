package com.shu.icpc.Component.mq;

import org.apache.shiro.SecurityUtils;

import java.io.Serializable;

public class Mail implements Serializable {
    private String email;
    private String content;
    private String title;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
