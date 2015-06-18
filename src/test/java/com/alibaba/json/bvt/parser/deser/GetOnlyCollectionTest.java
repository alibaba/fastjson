package com.alibaba.json.bvt.parser.deser;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import junit.framework.TestCase;

public class GetOnlyCollectionTest extends TestCase {

    public void test_getOnly() throws Exception {
        VO vo = JSON.parseObject("{\"items\":[\"a\",\"b\"]}", VO.class);
        Assert.assertEquals(2, vo.getItems().size());
        Assert.assertEquals("a", vo.getItems().get(0));
        Assert.assertEquals("b", vo.getItems().get(1));
    }

    public static class VO {
        private final List<String> items = new ArrayList<String>();

        public List<String> getItems() {
            return items;
        }
    }
}
