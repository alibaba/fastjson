package com.alibaba.fastjson.deserializer.issue4132;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.TypeUtils;
import lombok.Data;
import lombok.ToString;
import org.junit.Test;

/**
 * @author Rongzhen Yan
 */
public class TestIssue4132 {

    @Data
    @ToString
    static class DTO {

        @JSONField(name = "field_no1")
        private String fieldNo1;

        @JSONField(name = "field_no2")
        private String fieldNo2;
    }

    @Test
    public void fun1() {
        String json = "{\"fieldNo1\":\"value1\",\"fieldNo2\":\"value2\"}";
        DTO dto1 = JSON.parseObject(json, DTO.class);
        System.out.println(dto1);
    }

    @Test
    public void fun2() {
        System.out.println(TypeUtils.fnv1a_64_lower("field_no") == TypeUtils.fnv1a_64_lower("fieldNo"));
    }
}

