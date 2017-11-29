package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class Bug_for_issue_252 extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        String text = JSON.toJSONString(vo, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"type\":null}", text);
    }

    public static class VO {

        private Class<?> type;

        public Class<?> getType() {
            return type;
        }

        public void setType(Class<?> type) {
            this.type = type;
        }

    }
}
