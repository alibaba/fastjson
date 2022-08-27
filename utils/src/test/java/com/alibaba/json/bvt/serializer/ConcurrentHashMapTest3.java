package com.alibaba.json.bvt.serializer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class ConcurrentHashMapTest3 extends TestCase {

    public void test_concurrentHashmap() throws Exception {
        OffsetSerializeWrapper wrapper = new OffsetSerializeWrapper();
        wrapper.getOffsetTable().put(new MessageQueue(), new AtomicBoolean(true));
        String text = JSON.toJSONString(wrapper);
        Assert.assertEquals("{\"offsetTable\":{{\"items\":[]}:true}}", text);
        
        OffsetSerializeWrapper wrapper2 = JSON.parseObject(text, OffsetSerializeWrapper.class);
        Assert.assertEquals(1, wrapper2.getOffsetTable().size());
        
        Iterator<Map.Entry<MessageQueue, AtomicBoolean>> iter = wrapper2.getOffsetTable().entrySet().iterator();
        Map.Entry<MessageQueue, AtomicBoolean> entry = iter.next();
        Assert.assertEquals(0, entry.getKey().getItems().size());
        Assert.assertEquals(true, entry.getValue().get());
    }

    public static class OffsetSerializeWrapper {

        private ConcurrentHashMap<MessageQueue, AtomicBoolean> offsetTable = new ConcurrentHashMap<MessageQueue, AtomicBoolean>();

        public ConcurrentHashMap<MessageQueue, AtomicBoolean> getOffsetTable() {
            return offsetTable;
        }

        public void setOffsetTable(ConcurrentHashMap<MessageQueue, AtomicBoolean> offsetTable) {
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
