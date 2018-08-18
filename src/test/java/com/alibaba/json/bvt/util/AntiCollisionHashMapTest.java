package com.alibaba.json.bvt.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.AntiCollisionHashMap;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

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

        m.entrySet().remove(1);
        m.entrySet().remove(m.entrySet().iterator().next());



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
    }
}
