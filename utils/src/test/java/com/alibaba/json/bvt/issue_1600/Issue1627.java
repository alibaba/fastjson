package com.alibaba.json.bvt.issue_1600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

public class Issue1627 extends TestCase {
    public void test_for_issue() throws Exception {
        String a = "{\"101a0.test-b\":\"tt\"}";
        Object o = JSON.parse(a);
        String s = "101a0.test-b";
        assertTrue(JSONPath.contains(o, "$." + escapeString(s)));
    }

    public static String escapeString(String s) {
        StringBuilder buf = new StringBuilder();

        for(int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if((c < 48 || c > 57) && (c < 65 || c > 90) && (c < 97 || c > 122)) {
                buf.append("\\" + c);
            } else {
                buf.append(c);
            }
        }

        return buf.toString();
    }
}
