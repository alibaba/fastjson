package com.alibaba.json.bvt.serializer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class ConcurrentHashMapTest extends TestCase {

    public void test_concurrentHashmap() throws Exception {
        OffsetSerializeWrapper wrapper = new OffsetSerializeWrapper();
        wrapper.getOffsetTable().put(new MessageQueue(), new AtomicLong(123));
        String text = JSON.toJSONString(wrapper);
        Assert.assertEquals("{\"offsetTable\":{{\"items\":[]}:123}}", text);
        
        OffsetSerializeWrapper wrapper2 = JSON.parseObject(text, OffsetSerializeWrapper.class);
        Assert.assertEquals(1, wrapper2.getOffsetTable().size());
        
        Iterator<Map.Entry<MessageQueue, AtomicLong>> iter = wrapper2.getOffsetTable().entrySet().iterator();
        Map.Entry<MessageQueue, AtomicLong> entry = iter.next();
        Assert.assertEquals(0, entry.getKey().getItems().size());
        Assert.assertEquals(123L, entry.getValue().longValue());
    }

    public static class OffsetSerializeWrapper {

        private ConcurrentHashMap<MessageQueue, AtomicLong> offsetTable = new ConcurrentHashMap<MessageQueue, AtomicLong>();

        public ConcurrentHashMap<MessageQueue, AtomicLong> getOffsetTable() {
            return offsetTable;
        }

        public void setOffsetTable(ConcurrentHashMap<MessageQueue, AtomicLong> offsetTable) {
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
