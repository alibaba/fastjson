package com.alibaba.json.bvt.issue_3500;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.time.Instant;

public class Issue3539 extends TestCase {
    public void test_for_issue() throws Exception {
        String str = "{\"date\":{\"nano\":140000000,\"epochSecond\":1605106869}}";
        Bean bean = JSON.parseObject(str, Bean.class);
        assertNotNull(bean.date);
        JSON.toJSONString(bean);

        JSON.parseObject(str)
                .toJavaObject(Bean.class);
    }

    public void test_for_issue_joda() throws Exception {
        String str = "{\"date\":{\"epochSecond\":1605106869}}";
        JodaBean bean = JSON.parseObject(str, JodaBean.class);
        assertNotNull(bean.date);
        JSON.toJSONString(bean);

        JSON.parseObject(str)
                .toJavaObject(JodaBean.class);
    }

    public void test_for_issue_joda2() throws Exception {
        String str = "{\"date\":{\"millis\":1605364826724}}";
        JodaBean bean = JSON.parseObject(str, JodaBean.class);
        assertNotNull(bean.date);
        JSON.toJSONString(bean);

        JSON.parseObject(str)
                .toJavaObject(JodaBean.class);
    }

    public static class Bean {
        public Instant date;
    }

    public static class JodaBean {
        public org.joda.time.Instant date;
    }
}
