package com.alibaba.json.bvt.parser.str;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 13/03/2017.
 */
public class EmptyStringTest extends TestCase {
    public void test_for_emptyString() throws Exception {
        SolutionIdentifier solutionIdentifier = JSON.parseObject("{\"id\":\"\"}", SolutionIdentifier.class);
        assertNull(solutionIdentifier.id);
    }

    public static class SolutionIdentifier {
        public Id id;
    }

    public static class Id {
        public String id;
    }
}
