package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;

public class PrePropertyFilterTest extends TestCase {

    public void test_0() throws Exception {
        class VO {
            public int getId() { throw new RuntimeException(); }
        }

        PropertyPreFilter filter = new PropertyPreFilter () {
            public boolean apply(JSONSerializer serializer, Object source, String name) {
                return false;
            }
        };

        VO vo = new VO();

        String text = JSON.toJSONString(vo, filter);
        Assert.assertEquals("{}", text);
    }

}
