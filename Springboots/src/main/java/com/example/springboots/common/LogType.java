package com.example.springboots.common;

public enum LogType {
    ADD("新增"), DELETE("删除"), UPDATE("修改"), LOGIN("登陆"),REGISTER("注册"),BATCH_DELETE("批量删除");

    private String value;

    public String getValue() {
        return value;
    }

    LogType(String value) {
        this.value = value;
    }
}
