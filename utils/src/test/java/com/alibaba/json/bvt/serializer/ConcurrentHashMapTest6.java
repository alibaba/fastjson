package com.alibaba.json.bvt.serializer;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class ConcurrentHashMapTest6 extends TestCase {

    public void test_concurrentHashmap() throws Exception {
        OffsetSerializeWrapper wrapper = new OffsetSerializeWrapper();
        wrapper.offsetTable.put(new MessageQueue(), new WeakReference<A>(new A(true)));
        String text = JSON.toJSONString(wrapper);
        Assert.assertEquals("{\"offsetTable\":{{\"items\":[]}:{\"value\":true}}}", text);

        OffsetSerializeWrapper wrapper2 = JSON.parseObject(text, OffsetSerializeWrapper.class);
        Assert.assertEquals(1, wrapper2.getOffsetTable().size());

        Iterator<Map.Entry<MessageQueue, WeakReference<A>>> iter = wrapper2.getOffsetTable().entrySet().iterator();
        Map.Entry<MessageQueue, WeakReference<A>> entry = iter.next();
        Assert.assertEquals(0, entry.getKey().getItems().size());
        Assert.assertEquals(true, entry.getValue().get().isValue());
    }

    public static class OffsetSerializeWrapper {

        private ConcurrentHashMap<MessageQueue, WeakReference<A>> offsetTable = new ConcurrentHashMap<MessageQueue, WeakReference<A>>();

        public ConcurrentHashMap<MessageQueue, WeakReference<A>> getOffsetTable() {
            return offsetTable;
        }

        public void setOffsetTable(ConcurrentHashMap<MessageQueue, WeakReference<A>> offsetTable) {
            this.offsetTable = offsetTable;
        }

    }

    public static class MessageQueue {

        private List<Serializable> items = new LinkedList<Serializable>();

        public List<Serializable> getItems() {
            return items;
        }

    }

    public static class A implements Serializable {

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
