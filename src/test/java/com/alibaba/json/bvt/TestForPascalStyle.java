package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestForPascalStyle extends TestCase {

    public void test_for_pascal_style() throws Exception {
        String text = "{\"ID\":12,\"Name\":\"Jobs\"}";
        VO vo = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo.getId(), 12);
        Assert.assertEquals(vo.getName(), "Jobs");
    }

    public static class VO {

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
