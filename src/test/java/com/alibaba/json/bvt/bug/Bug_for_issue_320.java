package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class Bug_for_issue_320 extends TestCase {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void test_for_issue() throws Exception {
        Map map = new HashMap();
        map.put(1001L, "aaa");
        
        String text = JSON.toJSONString(map);
        Assert.assertEquals("{1001:\"aaa\"}", text);
        
        JSONObject obj = JSON.parseObject(text);
        Assert.assertEquals("aaa", obj.get("1001"));
    }

}
