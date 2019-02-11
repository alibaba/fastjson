package com.alibaba.json.bvt.issue_2200;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvt.issue_2200.issue2224.PersonCollection;
import com.alibaba.json.bvt.issue_2200.issue2224.PersonNameGroupedCollection;
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

    public void test_for_parameterized_item() {
        String json = "[[{\"idNo\":\"123\",\"name\":\"张三\"},{\"idNo\":\"124\",\"name\":\"张三\"}],[{\"idNo\":\"223\",\"name\":\"李四\"},{\"idNo\":\"224\",\"name\":\"李四\"}]]";
        PersonNameGroupedCollection personCollection = JSON.parseObject(json, PersonNameGroupedCollection.class);
        assertNotNull(personCollection);
        assertEquals(2, personCollection.size());
        assertEquals(2, personCollection.get("张三").size());
        assertEquals("123", personCollection.get("张三").get(0).getIdNo());
        assertEquals("张三", personCollection.get("张三").get(0).getName());
        assertEquals("124", personCollection.get("张三").get(1).getIdNo());
        assertEquals("张三", personCollection.get("张三").get(1).getName());
        assertEquals(2, personCollection.get("李四").size());
        assertEquals("223", personCollection.get("李四").get(0).getIdNo());
        assertEquals("李四", personCollection.get("李四").get(0).getName());
        assertEquals("224", personCollection.get("李四").get(1).getIdNo());
        assertEquals("李四", personCollection.get("李四").get(1).getName());
        String json2 = JSON.toJSONString(personCollection);
        assertNotNull(json2);
    }
}
