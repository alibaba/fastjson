package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;
import org.springframework.util.LinkedMultiValueMap;

/**
 * Created by wenshao on 01/06/2017.
 */
public class Issue1240 extends TestCase {
    public void test_for_issue() throws Exception {
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.setAutoTypeSupport(true);
        LinkedMultiValueMap<String, String> result = new LinkedMultiValueMap();
        result.add("test", "11111");
        String test = JSON.toJSONString(result, SerializerFeature.WriteClassName);
        JSON.parseObject(test, Object.class, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
    }
}
