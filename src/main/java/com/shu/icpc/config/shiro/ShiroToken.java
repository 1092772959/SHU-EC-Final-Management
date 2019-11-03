package com.shu.icpc.config.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class ShiroToken extends UsernamePasswordToken {
    public enum Type {
        Student,
        Coach,
        Admin,
    }

    private Type type;

    public ShiroToken() {

    }

    public ShiroToken(String userName, String password, Type type) {
        super(userName, password);
        this.type = type;
    }

    public ShiroToken(String userName, String password) {
        super(userName, password);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
