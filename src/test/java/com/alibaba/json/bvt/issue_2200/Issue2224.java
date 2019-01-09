package com.alibaba.json.bvt.issue_2200;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvt.issue_2200.issue2224.PersonCollection;
import junit.framework.TestCase;

public class Issue2224 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "[{\"idNo\":\"123456\",\"name\":\"tom\"},{\"idNo\":\"123457\",\"name\":\"jack\"}]";
        PersonCollection personCollection = JSON.parseObject(json, PersonCollection.class);
        assertNotNull(personCollection);
        assertEquals(2, personCollection.size());
        assertEquals("tom", personCollection.get("123456").getName());
        assertEquals("jack", personCollection.get("123457").getName());
        String json2 = JSON.toJSONString(personCollection);
        assertNotNull(json2);
    }
}
