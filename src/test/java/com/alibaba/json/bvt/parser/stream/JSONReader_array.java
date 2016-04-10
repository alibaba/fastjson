package com.alibaba.json.bvt.parser.stream;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONReader;

public class JSONReader_array extends TestCase {

    public void test_array() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("[[],[],3,null,{\"name\":\"jobs\"},{\"id\":123},{\"id\":1},{\"id\":2}]"));
        reader.startArray();

        JSONArray first = (JSONArray) reader.readObject();
        JSONArray second = (JSONArray) reader.readObject();

        Assert.assertNotNull(first);
        Assert.assertNotNull(second);

        Assert.assertEquals(new Integer(3), reader.readInteger());
        Assert.assertNull(reader.readString());
        
        {
            Map<String, Object> map = new HashMap<String, Object>();
            reader.readObject(map);
            Assert.assertEquals("jobs", map.get("name"));
        }

        {
            VO vo = new VO();
            reader.readObject(vo);
            Assert.assertEquals(123, vo.getId());
        }
        
        while (reader.hasNext()) {
            VO vo = reader.readObject(VO.class);
            Assert.assertNotNull(vo);
        }
        reader.endArray();
        reader.close();
    }

    public static class VO {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
