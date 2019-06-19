package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JSONObjectTest_readObject extends TestCase {
    public void test_0() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 123);
        jsonObject.put("obj", new JSONObject());

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
        objOut.writeObject(jsonObject);
        objOut.flush();

        byte[] bytes = bytesOut.toByteArray();

        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
        ObjectInputStream objIn = new ObjectInputStream(bytesIn);

        Object obj = objIn.readObject();

        assertEquals(JSONObject.class, obj.getClass());
        assertEquals(jsonObject, obj);
    }

    public void test_2() throws Exception {
        JSONObject jsonObject = JSON.parseObject("{123:345}");

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
        objOut.writeObject(jsonObject);
        objOut.flush();

        byte[] bytes = bytesOut.toByteArray();

        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
        ObjectInputStream objIn = new ObjectInputStream(bytesIn);

        Object obj = objIn.readObject();

        assertEquals(JSONObject.class, obj.getClass());
        assertEquals(jsonObject, obj);
    }

    public void test_3() throws Exception {
        JSONObject jsonObject = JSON.parseObject("{123:345,\"items\":[1,2,3,4]}");

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
        objOut.writeObject(jsonObject);
        objOut.flush();

        byte[] bytes = bytesOut.toByteArray();

        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
        ObjectInputStream objIn = new ObjectInputStream(bytesIn);

        Object obj = objIn.readObject();

        assertEquals(JSONObject.class, obj.getClass());
        assertEquals(jsonObject, obj);
    }

    public void test_4() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("val", new Byte[]{});

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
        objOut.writeObject(jsonObject);
        objOut.flush();

        byte[] bytes = bytesOut.toByteArray();

        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
        ObjectInputStream objIn = new ObjectInputStream(bytesIn);

        Object obj = objIn.readObject();

        assertEquals(JSONObject.class, obj.getClass());
        assertEquals(jsonObject.toJSONString(), JSON.toJSONString(obj));
    }

    public void test_5() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("val", new byte[]{});

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
        objOut.writeObject(jsonObject);
        objOut.flush();

        byte[] bytes = bytesOut.toByteArray();

        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
        ObjectInputStream objIn = new ObjectInputStream(bytesIn);

        Object obj = objIn.readObject();

        assertEquals(JSONObject.class, obj.getClass());
        assertEquals(jsonObject.toJSONString(), JSON.toJSONString(obj));
    }

    public void test_6() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("val", new Character[]{});
        jsonObject.put("cls", java.lang.Number.class);
        jsonObject.put("nums", new java.lang.Number[] {});

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
        objOut.writeObject(jsonObject);
        objOut.flush();

        byte[] bytes = bytesOut.toByteArray();

        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
        ObjectInputStream objIn = new ObjectInputStream(bytesIn);

        Object obj = objIn.readObject();

        assertEquals(JSONObject.class, obj.getClass());
        assertEquals(jsonObject.toJSONString(), JSON.toJSONString(obj));
    }
}
