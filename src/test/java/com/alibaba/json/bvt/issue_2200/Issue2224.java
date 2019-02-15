package com.alibaba.json.bvt.issue_2200;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvt.issue_2200.issue2224.PersonCollection;
import com.alibaba.json.bvt.issue_2200.issue2224_2.PersonGroupedCollection;
import junit.framework.TestCase;

public class Issue2224 extends TestCase {
    //support inherit with other parameterized type
    public void test_for_issue() {
        String json = "[{\"idNo\":\"123456\",\"name\":\"tom\"},{\"idNo\":\"123457\",\"name\":\"jack\"}]";
        PersonCollection personCollection = JSON.parseObject(json, PersonCollection.class);
        assertNotNull(personCollection);
        assertEquals(2, personCollection.size());
        assertEquals("tom", personCollection.get("123456").getName());
        assertEquals("jack", personCollection.get("123457").getName());
        String json2 = JSON.toJSONString(personCollection);
        assertNotNull(json2);
    }

    //support inherit with other parameterized type and item type is generic
    public void test_for_issue_2() {
        String json = "[[{\"idNo\":\"123\",\"name\":\"张三\"},{\"idNo\":\"124\",\"name\":\"张三\"}],[{\"idNo\":\"223\",\"name\":\"李四\"},{\"idNo\":\"224\",\"name\":\"李四\"}]]";
        PersonGroupedCollection personCollection = JSON.parseObject(json, PersonGroupedCollection.class);
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
