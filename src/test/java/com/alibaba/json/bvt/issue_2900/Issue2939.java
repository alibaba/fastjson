package com.alibaba.json.bvt.issue_2900;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Issue2939 extends TestCase {
    public void test_for_issue() throws Exception {

        LinkedMultiValueMap multiValueMap = new LinkedMultiValueMap();
        multiValueMap.add("k1","k11");
        multiValueMap.add("k1","k12");
        multiValueMap.add("k1","k13");
        multiValueMap.add("k2","k21");


        String json = JSON.toJSONString(multiValueMap);
        assertEquals("{\"k1\":[\"k11\",\"k12\",\"k13\"],\"k2\":[\"k21\"]}", json);


        Object obj = JSON.parseObject(json, LinkedMultiValueMap.class);
        assertTrue(obj != null);

        LinkedMultiValueMap map = (LinkedMultiValueMap) obj;
        assertSame(3, map.get("k1").size());

    }

}
