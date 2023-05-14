package com.alibaba.json.bvt.issue_1100;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

/**
 * Created by wenshao on 01/04/2017.
 */
public class Issue1121 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONObject userObject = new JSONObject();
        userObject.put("name","jack");
        userObject.put("age",20);

        JSONObject result = new JSONObject();
        result.put("host","127.0.0.1");
        result.put("port",3306);
        result.put("user",userObject);
        result.put("admin",userObject);

        String json = JSON.toJSONString(result, true);
        System.out.println(json);

        JSONObject jsonObject2 = JSON.parseObject(json);
        assertEquals(result, jsonObject2);
    }
}
