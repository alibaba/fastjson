package com.alibaba.json.bvt.issue_3500;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class Issue3544 extends TestCase {

    public void test_errorType() {
        assertNull("", JSON.toJavaObject(
                JSON.parseObject("{\"result\":\"\"}"), TestVO.class).result);

        assertNull("", JSON.toJavaObject(
                JSON.parseObject("{\"result\":\"null\"}"), TestVO.class).result);
    }

    @Getter
    @Setter
    static class TestVO {

        Map<String, String> result;

    }
}
