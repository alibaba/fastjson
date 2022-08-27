package com.alibaba.json.bvt.issue_2300;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

public class Issue2306 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONObject object = new JSONObject();
        object.put("help_score_avg.cbm", 123);

        assertEquals(123
            , JSONPath.extract(
                    object.toJSONString(), "['help_score_avg.cbm']"));
    }
}
