package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

public class Bug_for_chengchao_1 extends TestCase {
    public void test_0() throws Exception {
        ParserConfig config = new ParserConfig();
        config.setAutoTypeSupport(true);
        String str = "{\"@type\":\"test.MapDone\",\"data\":{\"@type\":\"test.HiluxDataByOpsmeta\",\"attends\":{\"@type\":\"java.util.HashMap\",\"center.na61\":2},\"datasByOpsmeta\":{\"@type\":\"java.util.HashMap\",{\"@type\":\"test.AppInst\",\"app\":\"wdkhummer\",\"appGroup\":\"wdkhummerhost\",\"env\":\"PUBLISH\",\"hostname\":\"wdkhummer011009059229.na61\",\"idc\":\"na61\",\"ip\":\"11.9.59.229\",\"online\":true}:{\"@type\":\"test.MiddlewareDimData\",\"attends\":{\"@type\":\"java.util.HashMap\"},\"expectAttends\":{\"@type\":\"java.util.HashMap\"},\"logLineCount\":0,\"values\":{}}}}}";
        JSON.parse(str, config, JSON.DEFAULT_PARSER_FEATURE);
    }
}
