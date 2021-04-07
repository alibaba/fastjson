package com.alibaba.json.bvt.issue_3600;

import com.alibaba.fastjson.JSONValidator;
import org.junit.Test;

public class Issue3603 {
    @Test
    public void test1() {
        String json = "{   \"5\":   [   \"award\",  \"1041067\"   ]}";
        JSONValidator.from(json).validate();
    }
}
