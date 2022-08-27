package com.alibaba.json.bvt.parser.stream;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONReaderScanner;

public class JSONReaderScannerTest_matchField extends TestCase {

    public void test_true() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser(new JSONReaderScanner("{\"items\":[{}],\"value\":{}}"));
        VO vo = parser.parseObject(VO.class);
        Assert.assertNotNull(vo.getValue());
        Assert.assertNotNull(vo.getItems());
        Assert.assertEquals(1, vo.getItems().size());
        Assert.assertNotNull(vo.getItems().get(0));
        parser.close();
    }

    public static class VO {

        private List<Item> items;
        private Entity     value;

        public Entity getValue() {
            return value;
        }

        public void setValue(Entity value) {
            this.value = value;
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

    }

    public static class Entity {

    }

    public static class Item {

    }
}
