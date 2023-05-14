package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

/**
 * Created by kimmking on 20/06/2017.
 */
public class Issue1278 extends TestCase {
    public void test_for_issue() throws Exception {

        String json1 = "{\"name\":\"name\",\"id\":1}";
        String json2 = "{\"user\":\"user\",\"id\":2}";
        AlternateNames c1 = JSON.parseObject(json1, AlternateNames.class);

        assertEquals("name",c1.name);
        assertEquals(1,c1.id);

        AlternateNames c2 = JSON.parseObject(json2, AlternateNames.class);

        assertEquals("user",c2.name);
        assertEquals(2,c2.id);

        DefaultJSONParser parser = new DefaultJSONParser(json1);
        c1 = new AlternateNames();
        parser.parseObject(c1);

        assertEquals("name",c1.name);
        assertEquals(1,c1.id);

        c2 = new AlternateNames();
        parser = new DefaultJSONParser(json2);
        parser.parseObject(c2);

        assertEquals("user",c2.name);
        assertEquals(2,c2.id);

        JSONObject jsonObject = JSON.parseObject(json1);
        c1 = jsonObject.toJavaObject(AlternateNames.class);

        assertEquals("name",c1.name);
        assertEquals(1,c1.id);

        jsonObject = JSON.parseObject(json2);
        c2 = jsonObject.toJavaObject(AlternateNames.class);

        assertEquals("user",c2.name);
        assertEquals(2,c2.id);

    }

    /**
     * {"name":"name","id":1}
     * {"user":"user","id":2}
     */
    public static class AlternateNames {
        @JSONField(alternateNames = {"name", "user"})
        public String name;
        public int id;
    }


}
