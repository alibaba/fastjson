package com.alibaba.json.bvt.issue_2300;

import com.alibaba.fastjson.JSONPath;
import com.jayway.jsonpath.JsonPath;
import junit.framework.TestCase;

public class Issue2311 extends TestCase {
    public void test_for_issue() throws Exception {
        String t = "{\"groups\":[{\"timers\":[{\"date\":\"00000001\",\"dps\":{\"1\":true},\"loops\":\"1111111\",\"timezoneId\":\"Asia/Shanghai\",\"time\":\"13:06\",\"status\":1},{\"date\":\"00000010\",\"dps\":{\"1\":true},\"loops\":\"1111111\",\"timezoneId\":\"Asia/Shanghai\",\"time\":\"13:07\",\"status\":1}],\"id\":\"1:\"},{\"timers\":[{\"date\":\"00000100\",\"dps\":{\"1\":true},\"loops\":\"1111111\",\"timezoneId\":\"Asia/Shanghai\",\"time\":\"13:06\",\"status\":1},{\"date\":\"00001000\",\"dps\":{\"1\":true},\"loops\":\"1111111\",\"timezoneId\":\"Asia/Shanghai\",\"time\":\"13:07\",\"status\":1}],\"id\":\"2:\"}],\"category\":{\"category\":\"xxxxxx\",\"status\":1}}";
        System.out.println((Object) JsonPath.read(t, "$.groups[*].timers[*].dps.1"));
        System.out.println(JSONPath.extract(t, "$.groups[*].timers[*].dps['1']"));
    }
}
