package com.alibaba.fastjson.serializer.issues3601;

import lombok.Data;

@Data
public class TestEntity {
    private TestEnum testEnum;
    private String testName;
}
