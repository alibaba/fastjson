package com.alibaba.json.bvt.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.AntiCollisionHashMap;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map;

public class AntiCollisionHashMapTest extends TestCase {
    public void test_0() throws Exception {
        AntiCollisionHashMap m = new AntiCollisionHashMap(3, 0.75f);

        for (int i = 0; i < 100; ++i) {
            m.put(i, i);
        }

        AntiCollisionHashMap m2 = new AntiCollisionHashMap(m);
        assertEquals(m.size(), m2.size());

        AntiCollisionHashMap m3 = new AntiCollisionHashMap(3, 0.75f);
        m3.putAll(m);
        assertEquals(m.size(), m2.size());

        AntiCollisionHashMap m4 = (AntiCollisionHashMap) m.clone();
        m4.hashCode();
        m4.size();
        m4.isEmpty();
        m4.values().iterator();
        m4.keySet().iterator();
        m4.values().contains(1);
        m4.values().contains(null);
        m4.values().iterator().next();
        m4.values().remove(1);
        m4.values().size();
        m4.values().clear();

        AntiCollisionHashMap m5 = (AntiCollisionHashMap) m.clone();
        m5.keySet().contains(1);
        m5.put(1, 1001);
        m5.get(null);
        Map.Entry entry = (Map.Entry) m5.entrySet().iterator().next();
        entry.setValue(1002);
        entry.toString();
        m5.keySet().size();
        m5.keySet().iterator().next();
        m5.keySet().remove(1);
        m5.keySet().clear();

        AntiCollisionHashMap m6 = new AntiCollisionHashMap(3);
        m6.putAll(m);
        assertEquals(m.size(), m6.size());
        m6.put("a", "a");
        m6.put("b", "b");

        for (int i = 0; i < 100; ++i) {
            assertEquals(i, m.get(i));
            assertTrue(m.containsKey(i));
        }

        for (int i = 0; i < 100; ++i) {
            m3.remove(i);
            m2.remove(i, i);
        }
        m2.put(null, null);
        m2.put(1, 1);
        assertTrue(m2.containsKey(null));
        assertTrue(m2.containsKey(1));
        assertTrue(m2.containsValue(null));
        assertTrue(m2.containsValue(1));
        Iterator iterator = m2.entrySet().iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        m2.clear();

        assertFalse(m.entrySet().contains(1));
        assertTrue(m.entrySet().contains(m.entrySet().iterator().next()));
        m.entrySet().size();
        m.entrySet().remove(1);
        m.entrySet().remove(m.entrySet().iterator().next());
        m.entrySet().clear();

        {
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
            objOut.writeObject(m);
            objOut.flush();

            byte[] bytes = bytesOut.toByteArray();

            ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
            ObjectInputStream objIn = new ObjectInputStream(bytesIn);

            Object obj = objIn.readObject();

            assertEquals(AntiCollisionHashMap.class, obj.getClass());
            assertEquals(m, obj);
        }
        {
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
            objOut.writeObject(m6);
            objOut.flush();

            byte[] bytes = bytesOut.toByteArray();

            ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
            ObjectInputStream objIn = new ObjectInputStream(bytesIn);

            Object obj = objIn.readObject();

            assertEquals(AntiCollisionHashMap.class, obj.getClass());
            assertEquals(m6, obj);
        }
    }
}
