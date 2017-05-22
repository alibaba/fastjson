package com.alibaba.json.bvt;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class JSONObjectTest_get extends TestCase {

    public void test_get() throws Exception {
        JSONObject obj = JSON.parseObject("{id:123}");
        Assert.assertEquals(123, obj.getObject("id", Object.class));
    }

    public static interface VO {
        @JSONField()
        int getId();

        @JSONField()
        void setId(int val);
    }
}
