package com.alibaba.json.bvt;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class JSONObjectTest4 extends TestCase {

    public void test_interface() throws Exception {
        VO vo = JSON.parseObject("{id:123}", VO.class);
        Assert.assertEquals(123, vo.getId());
    }

    public static interface VO {
        @JSONField()
        int getId();

        @JSONField()
        void setId(int val);
    }
}
