package com.alibaba.json.bvt.bug.bug2020;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.List;

public class Bug_for_emptyList extends TestCase {
    public void test_for_issue() throws Exception {
        String str = "{\"values\":[1,2,3]}";
        VO vo = JSON.parseObject(str, VO.class);
        assertEquals(3, vo.values.size());
    }

    public static class VO {
        private java.util.List values = kotlin.collections.CollectionsKt.emptyList();

        public List getValues()
        {
            return values;
        }
    }
}
