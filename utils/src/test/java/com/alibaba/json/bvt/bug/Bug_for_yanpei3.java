package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class Bug_for_yanpei3 extends TestCase {
    public void test_for_issue() throws Exception {
        Map obj = new HashMap();
        obj.put("desc", "\"Puck\"");
        String text = JSON.toJSONString(obj);
//        System.out.println(text);
        // {"desc":"\"Puck\""}

        Map root = new HashMap();
        root.put("obj", text);
        String text2 = JSON.toJSONString(root);
//        System.out.println(text2);
        // {"obj":"{\"desc\":\"\\\"Puck\\\"\"}"}

        JSONObject root2 = JSON.parseObject(text2);
        String text3 = (String) root2.get("obj");
//        System.out.println(text3);
        // {"desc":"\"Puck\""}

        JSONObject obj2 = JSON.parseObject(text3);
        String puck = (String) obj2.get("desc");
        Assert.assertEquals(obj.get("desc"), obj2.get("desc"));
        // "Puck"
    }
}
