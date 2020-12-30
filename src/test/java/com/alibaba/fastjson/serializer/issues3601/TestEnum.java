package com.alibaba.fastjson.serializer.issues3601;

import com.alibaba.fastjson.annotation.JSONField;

public enum TestEnum {

    @JSONField
    test1("1"),
//    @JSONField
    test2("2");

    private String title;

    TestEnum(String title) {
        this.title = title;
    }

//    @JSONField
    public String getTitle() {
        return title;
    }

    private Integer i = 100;

    @JSONField
    public Integer getI() {
        return i;
    }

}


