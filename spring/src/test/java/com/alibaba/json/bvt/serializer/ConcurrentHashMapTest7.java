package com.alibaba.json.bvt.serializer;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class ConcurrentHashMapTest7 extends TestCase {

    public void test_concurrentHashmap() throws Exception {
        OffsetSerializeWrapper wrapper = new OffsetSerializeWrapper();
        wrapper.getOffsetTable().put(new MessageQueue(), new SoftReference<A>(new A(true)));
        String text = JSON.toJSONString(wrapper);
        Assert.assertEquals("{\"offsetTable\":{{\"items\":[]}:{\"value\":true}}}", text);

        OffsetSerializeWrapper wrapper2 = JSON.parseObject(text, OffsetSerializeWrapper.class);
        Assert.assertEquals(1, wrapper2.getOffsetTable().size());

        Iterator<Map.Entry<MessageQueue, SoftReference<A>>> iter = wrapper2.getOffsetTable().entrySet().iterator();
        Map.Entry<MessageQueue, SoftReference<A>> entry = iter.next();
        Assert.assertEquals(0, entry.getKey().getItems().size());
        Assert.assertEquals(true, entry.getValue().get().isValue());
    }

    public static class OffsetSerializeWrapper {

        private ConcurrentHashMap<MessageQueue, SoftReference<A>> offsetTable = new ConcurrentHashMap<MessageQueue, SoftReference<A>>();

        public ConcurrentHashMap<MessageQueue, SoftReference<A>> getOffsetTable() {
            return offsetTable;
        }

        public void setOffsetTable(ConcurrentHashMap<MessageQueue, SoftReference<A>> offsetTable) {
            this.offsetTable = offsetTable;
        }

    }

    public static class MessageQueue {

        private List<Object> items = new LinkedList<Object>();

        public List<Object> getItems() {
            return items;
        }

    }

    public static class A {

        private boolean value;

        public A(){

        }

        public A(boolean value){
            super();
            this.value = value;
        }

        public boolean isValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }

    }
}
