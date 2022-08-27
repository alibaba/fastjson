package com.alibaba.json.bvt.parser.stream;

import java.io.StringReader;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONReader;

public class JSONReader_obj_2 extends TestCase {

    public void test_array() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("[{\"id\":123}]"));

        reader.startArray();

        VO vo = reader.readObject(VO.class);

        Assert.assertEquals(123, vo.getId());

        reader.endArray();

        reader.close();
    }

    public void test_obj() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{\"id\":123}"));

        VO vo = reader.readObject(VO.class);

        Assert.assertEquals(123, vo.getId());

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
