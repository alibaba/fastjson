package com.alibaba.json.bvt.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.AntiCollisionHashMap;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

        for (int i = 0; i < 100; ++i) {
            m3.remove(i);
            m2.remove(i, i);
        }

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
