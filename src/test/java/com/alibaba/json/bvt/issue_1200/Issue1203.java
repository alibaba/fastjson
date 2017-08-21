package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 16/05/2017.
 */
public class Issue1203 extends TestCase {
    public void test_for_issue() throws Exception {
        String[] strArr = new String[5];
        strArr[0] = "a";
        strArr[1] = "b";
        strArr[3] = "d";
        strArr[4] = "";

        String json = JSON.toJSONString(strArr, SerializerFeature.WriteNullStringAsEmpty);
        assertEquals("[\"a\",\"b\",\"\",\"d\",\"\"]", json);
    }
}
