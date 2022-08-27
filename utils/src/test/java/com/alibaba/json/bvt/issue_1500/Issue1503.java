package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class Issue1503 extends TestCase {
    public void test_for_issue() throws Exception {
        ParserConfig config = new ParserConfig();
        config.setAutoTypeSupport(true);
        Map<Long, Bean> map = new HashMap<Long, Bean>();
        map.put(null, new Bean());
        Map<Long, Bean> rmap = (Map<Long, Bean>) JSON.parse(JSON.toJSONString(map, SerializerFeature.WriteClassName), config);
        System.out.println(rmap);
    }

    public static class Bean {

    }
}
