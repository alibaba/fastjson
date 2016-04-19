package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class Bug_for_issue_352 extends TestCase {
    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.name = "张三";
        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"index\":0,\"名\":\"张三\"}", text);
        VO v1 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo.name, v1.name);
    }
    
    public static class VO {
        public int index;
        @JSONField(name="名")
        public String name;
    }
}
