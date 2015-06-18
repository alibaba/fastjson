package com.alibaba.json.bvt.serializer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class ConcurrentHashMapTest2 extends TestCase {

    public void test_concurrentHashmap() throws Exception {
        OffsetSerializeWrapper wrapper = new OffsetSerializeWrapper();
        wrapper.getOffsetTable().put(new MessageQueue(), new AtomicInteger(123));
        String text = JSON.toJSONString(wrapper);
        Assert.assertEquals("{\"offsetTable\":{{\"items\":[]}:123}}", text);
        
        OffsetSerializeWrapper wrapper2 = JSON.parseObject(text, OffsetSerializeWrapper.class);
        Assert.assertEquals(1, wrapper2.getOffsetTable().size());
        
        Iterator<Map.Entry<MessageQueue, AtomicInteger>> iter = wrapper2.getOffsetTable().entrySet().iterator();
        Map.Entry<MessageQueue, AtomicInteger> entry = iter.next();
        Assert.assertEquals(0, entry.getKey().getItems().size());
        Assert.assertEquals(123, entry.getValue().intValue());
    }

    public static class OffsetSerializeWrapper {

        private ConcurrentHashMap<MessageQueue, AtomicInteger> offsetTable = new ConcurrentHashMap<MessageQueue, AtomicInteger>();

        public ConcurrentHashMap<MessageQueue, AtomicInteger> getOffsetTable() {
            return offsetTable;
        }

        public void setOffsetTable(ConcurrentHashMap<MessageQueue, AtomicInteger> offsetTable) {
            this.offsetTable = offsetTable;
        }

    }

    public static class MessageQueue {

        private List<Object> items = new LinkedList<Object>();

        public List<Object> getItems() {
            return items;
        }

    }
}
