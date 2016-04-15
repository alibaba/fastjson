package com.alibaba.json.bvt.android;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class MFieldTest extends TestCase {

    public void test_mfield() throws Exception {
        VO vo = new VO();
        vo.setName("xxx");
        
        String text = JSON.toJSONString(vo);
        
        Assert.assertEquals("{\"new_message\":\"xxx\"}", text);
        
        VO vo1 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo.mName, vo1.mName);
    }

    public static class VO {

        @JSONField(name = "new_message")
        private String mName;

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            this.mName = name;
        }

    }
}
