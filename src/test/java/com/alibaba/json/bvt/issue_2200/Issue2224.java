package com.alibaba.json.bvt.issue_2200;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvt.issue_2200.issue2224.PersonCollection;
import com.alibaba.json.bvt.issue_2200.issue2224_2.PersonGroupedCollection;
import com.alibaba.json.bvt.issue_2200.issue2224_3.ArrayPersonGroupedCollection;
import com.alibaba.json.bvt.issue_2200.issue2224_4.MAPersonGroupedCollection;
import com.alibaba.json.bvt.issue_2200.issue2224_5.MA2PersonGroupedCollection;
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

    //support inherit with other parameterized type and item type is bean array
    public void test_for_issue_3() {
        String json = "[[{\"idNo\":\"123\",\"name\":\"张三\"},{\"idNo\":\"124\",\"name\":\"张三\"}],[{\"idNo\":\"223\",\"name\":\"李四\"},{\"idNo\":\"224\",\"name\":\"李四\"}]]";
        ArrayPersonGroupedCollection personCollection = JSON.parseObject(json, ArrayPersonGroupedCollection.class);
        assertNotNull(personCollection);
        assertEquals(2, personCollection.size());
        assertEquals(2, personCollection.get("张三").length);
        assertEquals("123", personCollection.get("张三")[0].getIdNo());
        assertEquals("张三", personCollection.get("张三")[0].getName());
        assertEquals("124", personCollection.get("张三")[1].getIdNo());
        assertEquals("张三", personCollection.get("张三")[1].getName());
        assertEquals(2, personCollection.get("李四").length);
        assertEquals("223", personCollection.get("李四")[0].getIdNo());
        assertEquals("李四", personCollection.get("李四")[0].getName());
        assertEquals("224", personCollection.get("李四")[1].getIdNo());
        assertEquals("李四", personCollection.get("李四")[1].getName());
        String json2 = JSON.toJSONString(personCollection);
        assertNotNull(json2);
    }

    //support inherit with other parameterized type and item type is generic array
    public void test_for_issue_4() {
        String json = "[[{\"idNo\":\"123\",\"name\":\"张三\"},{\"idNo\":\"124\",\"name\":\"张三\"}],[{\"idNo\":\"223\",\"name\":\"李四\"},{\"idNo\":\"224\",\"name\":\"李四\"}]]";
        MAPersonGroupedCollection personCollection = JSON.parseObject(json, MAPersonGroupedCollection.class);
        assertNotNull(personCollection);
        assertEquals(2, personCollection.size());
        assertEquals(2, personCollection.get("张三").length);
        assertEquals("123", personCollection.get("张三")[0].get("idNo"));
        assertEquals("张三", personCollection.get("张三")[0].get("name"));
        assertEquals("124", personCollection.get("张三")[1].get("idNo"));
        assertEquals("张三", personCollection.get("张三")[1].get("name"));
        assertEquals(2, personCollection.get("李四").length);
        assertEquals("223", personCollection.get("李四")[0].get("idNo"));
        assertEquals("李四", personCollection.get("李四")[0].get("name"));
        assertEquals("224", personCollection.get("李四")[1].get("idNo"));
        assertEquals("李四", personCollection.get("李四")[1].get("name"));
        String json2 = JSON.toJSONString(personCollection);
        assertNotNull(json2);
    }

    //support inherit with other parameterized type and item type is generic array contains array
    public void test_for_issue_5() {
        String json = "[[{\"idNo\":[\"123\",\"123x\"],\"name\":[\"张三\",\"张三一\"]},{\"idNo\":[\"124\",\"124x\"],\"name\":[\"张三\",\"张三一\"]}],[{\"idNo\":[\"223\",\"223y\"],\"name\":[\"李四\",\"李小四\"]},{\"idNo\":[\"224\",\"224y\"],\"name\":[\"李四\",\"李小四\"]}]]";
        MA2PersonGroupedCollection personCollection = JSON.parseObject(json, MA2PersonGroupedCollection.class);
        assertNotNull(personCollection);
        assertEquals(2, personCollection.size());
        assertEquals(2, personCollection.get("张三").length);
        assertEquals(2, personCollection.get("张三")[0].get("idNo").length);
        assertEquals("123", personCollection.get("张三")[0].get("idNo")[0]);
        assertEquals("123x", personCollection.get("张三")[0].get("idNo")[1]);
        assertEquals(2, personCollection.get("张三")[0].get("name").length);
        assertEquals("张三", personCollection.get("张三")[0].get("name")[0]);
        assertEquals("张三一", personCollection.get("张三")[0].get("name")[1]);
        assertEquals(2, personCollection.get("张三")[1].get("idNo").length);
        assertEquals("124", personCollection.get("张三")[1].get("idNo")[0]);
        assertEquals("124x", personCollection.get("张三")[1].get("idNo")[1]);
        assertEquals(2, personCollection.get("张三")[1].get("name").length);
        assertEquals("张三", personCollection.get("张三")[1].get("name")[0]);
        assertEquals("张三一", personCollection.get("张三")[1].get("name")[1]);
        assertEquals(2, personCollection.get("李四").length);
        assertEquals(2, personCollection.get("李四")[0].get("idNo").length);
        assertEquals("223", personCollection.get("李四")[0].get("idNo")[0]);
        assertEquals("223y", personCollection.get("李四")[0].get("idNo")[1]);
        assertEquals(2, personCollection.get("李四")[0].get("name").length);
        assertEquals("李四", personCollection.get("李四")[0].get("name")[0]);
        assertEquals("李小四", personCollection.get("李四")[0].get("name")[1]);
        assertEquals(2, personCollection.get("李四")[1].get("idNo").length);
        assertEquals("224", personCollection.get("李四")[1].get("idNo")[0]);
        assertEquals("224y", personCollection.get("李四")[1].get("idNo")[1]);
        assertEquals(2, personCollection.get("李四")[1].get("name").length);
        assertEquals("李四", personCollection.get("李四")[1].get("name")[0]);
        assertEquals("李小四", personCollection.get("李四")[1].get("name")[1]);
        String json2 = JSON.toJSONString(personCollection);
        assertNotNull(json2);
    }
}
