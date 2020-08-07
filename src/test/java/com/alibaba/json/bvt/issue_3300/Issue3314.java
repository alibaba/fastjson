package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

public class Issue3314 extends TestCase {
    private Field field;
    protected void setUp() throws Exception {
        Class clazz = Class.forName("com.alibaba.fastjson.JSONObject$SecureObjectInputStream");
        field = clazz.getDeclaredField("fields_error");
        field.setAccessible(true);
        field.set(null, true);
    }

    protected void tearDown() throws Exception {
        field.set(null, false);
    }

    public void test_for_issue() throws Exception {
        JSONArray array = new JSONArray();
        array.add(1);
        array.add(null);
        array.add("wenshaojin");

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
        objOut.writeObject(array);
        objOut.flush();



        byte[] bytes = bytesOut.toByteArray();

        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
        ObjectInputStream objIn = new ObjectInputStream(bytesIn);

        Object obj = objIn.readObject();

        assertEquals(JSONArray.class, obj.getClass());
        assertEquals(array, obj);
    }
}
