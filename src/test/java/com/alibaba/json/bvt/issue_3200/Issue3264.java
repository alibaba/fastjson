package com.alibaba.json.bvt.issue_3200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.junit.Assert;

/**
 * @Author ：Nanqi
 * @Date ：Created in 21:25 2020/6/22
 */
public class Issue3264 extends TestCase {
    public void test_for_issue() throws Exception {
        MyData data = MyData.builder().isTest(true).build();
        String string = JSON.toJSONString(data);
        Assert.assertTrue(string.contains("is_test"));
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class MyData {
        @JSONField(name = "is_test")
        private Boolean isTest;
    }
}
