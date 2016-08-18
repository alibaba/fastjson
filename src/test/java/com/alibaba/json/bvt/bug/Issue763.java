package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import junit.framework.TestCase;

public class Issue763 extends TestCase {

    public void test_0() throws Exception {
        Map<String, Object> reqDto = new HashMap<String, Object>();
        reqDto.put("name", "aaaa");
        reqDto.put("age", 50);
        reqDto.put("address", "深圳南山");

        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("name");

        // SerializeConfig.getGlobalInstance().addFilter(Map.class, filter);
        SerializeConfig config = new SerializeConfig();
        config.addFilter(HashMap.class, filter);
        System.out.println(JSON.toJSONString(reqDto, config));
    }
}
