package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

public class IssueForJSONFieldMatch extends TestCase {
    public void test_for_issue() throws Exception {
        assertEquals(123
                , JSON.parseObject("{\"user_Id\":123}", VO.class)
                        .userId);
        assertEquals(123
                , JSON.parseObject("{\"userId\":123}", VO.class)
                        .userId);
        assertEquals(123
                , JSON.parseObject("{\"user-id\":123}", VO.class)
                        .userId);
    }

    public static class VO {
        @JSONField(name = "user_id", alternateNames = {"userId", "user-id"})
        public int userId;
    }
}
