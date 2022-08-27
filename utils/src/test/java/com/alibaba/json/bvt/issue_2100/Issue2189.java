package com.alibaba.json.bvt.issue_2100;

import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

public class Issue2189 extends TestCase {
    public void test_for_issue() throws Exception {
        String str = "[{\"id\":\"1\",\"name\":\"a\"},{\"id\":\"2\",\"name\":\"b\"}]";
        assertEquals("[\"1\",\"2\"]",
                JSONPath.extract(str, "$.*.id")
                        .toString()
        );
    }

    public void test_for_issue_1() throws Exception {
        String str = "[{\"id\":\"1\",\"name\":\"a\"},{\"id\":\"2\",\"name\":\"b\"}]";
        assertEquals("[\"2\"]",
                JSONPath.extract(str, "$.*[?(@.name=='b')].id")
                        .toString()
        );
    }
}
