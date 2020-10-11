package com.alibaba.json.bvt.issue_3400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class Issue3465 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONObject jsonObj1 = new JSONObject();
        JSONObject sonJsonObj1 = new JSONObject();
        sonJsonObj1.put("dca0898f74b4cc6d0174b4cc77fd0005", "2ca0898f74b4cc6d0174b4cc77fd0005");
        jsonObj1.put("issue", sonJsonObj1);
        String rst1 = JSON.toJSONString(jsonObj1, JSON.DEFAULT_GENERATE_FEATURE | SerializerFeature.WRITE_MAP_NULL_FEATURES);
        System.out.println(rst1);
        JSONObject parse1 = JSON.parseObject(rst1);
        System.out.println(parse1.toJSONString());


        JSONObject jsonObj = new JSONObject();
        JSONObject sonJsonObj = new JSONObject();
        sonJsonObj.put("2ca0898f74b4cc6d0174b4cc77fd0005", "2ca0898f74b4cc6d0174b4cc77fd0005");
        jsonObj.put("issue", sonJsonObj);
        String rst = JSON.toJSONString(jsonObj, JSON.DEFAULT_GENERATE_FEATURE | SerializerFeature.WRITE_MAP_NULL_FEATURES);
        System.out.println(rst);
        JSONObject parse = JSON.parseObject(rst);
        System.out.println(parse.toJSONString());
    }
}
