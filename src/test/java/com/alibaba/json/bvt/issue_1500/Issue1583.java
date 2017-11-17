package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Issue1583 extends TestCase {
    public void test_issue() throws Exception {
        Map<String, List<String>> totalMap = new HashMap<String, List<String>>();
        for (int i = 0; i < 10; i++) {
            List<String> list = new ArrayList<String>();
            for (int j = 0; j < 2; j++) {
                list.add("list" + j);
            }
            totalMap.put("map" + i, list);
        }
        List<Map.Entry<String, List<String>>> mapList = new ArrayList<Map.Entry<String, List<String>>>(totalMap.entrySet());
        String jsonString = JSON.toJSONString(mapList, SerializerFeature.DisableCircularReferenceDetect);

        System.out.println(jsonString);
        List<Map.Entry<String, List<String>>> parse = JSON.parseObject(jsonString, new TypeReference<List<Map.Entry<String, List<String>>>>() {});
        System.out.println(parse);
    }
}
