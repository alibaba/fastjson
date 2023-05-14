package com.alibaba.json.bvt.serializer.filters;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;

public class PropertyFilterTest2 extends TestCase {

    public void test_0() throws Exception {
        class VO {
            public int    id;
            public String name;
        }

        PropertyFilter filter = new PropertyFilter() {

            public boolean apply(Object source, String name, Object value) {
                return "id".equals(name);
            }
        };

        VO vo = new VO();
        vo.id = 123;
        vo.name = "gaotie";

        String text = JSON.toJSONString(vo, filter);
        Assert.assertEquals("{\"id\":123}", text);
    }

}
