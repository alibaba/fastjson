package com.alibaba.json.bvt.serializer;

import java.util.ArrayList;
import java.util.LinkedList;

import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ListSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class ListSerializerTest3 extends TestCase {

    public void test_1() throws Exception {
        SerializeWriter out = new SerializeWriter();
        ListSerializer listSerializer = new ListSerializer();

        ArrayList<Object> list = new ArrayList<Object>();
        for (int i = 0; i < 100000; i++) {
            list.add(i);
        }

        long start = System.currentTimeMillis();
        listSerializer.write(new JSONSerializer(out), list, null, null, 0);
        long end = System.currentTimeMillis();

        System.out.println("arrayList time: " + (end - start));
    }

    public void test_2() throws Exception {
        SerializeWriter out = new SerializeWriter();

        ListSerializer listSerializer = new ListSerializer();

        LinkedList<Object> list = new LinkedList<Object>();
        for (int i = 0; i < 100000; i++) {
            list.add(i);
        }

        long start = System.currentTimeMillis();
        listSerializer.write(new JSONSerializer(out), list, null, null, 0);
        long end = System.currentTimeMillis();

        System.out.println("linkedList time: " + (end - start));
    }

}
