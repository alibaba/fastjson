package com.alibaba.json.bvt.serializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ListSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class ListSerializerTest extends TestCase {

    public void test_0_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        ListSerializer listSerializer = new ListSerializer();
        listSerializer.write(new JSONSerializer(out), Collections.EMPTY_LIST, null, null, 0);

        Assert.assertEquals("[]", out.toString());
    }

    public void test_2_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        ListSerializer listSerializer = new ListSerializer();
        List<Object> list = new ArrayList<Object>();
        list.add(1);
        list.add(2);
        listSerializer.write(new JSONSerializer(out), list, null, null, 0);

        Assert.assertEquals("[1,2]", out.toString());
    }

    public void test_3_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        ListSerializer listSerializer = new ListSerializer();
        List<Object> list = new ArrayList<Object>();
        list.add(1);
        list.add(2);
        list.add(3);
        listSerializer.write(new JSONSerializer(out), list, null, null, 0);

        Assert.assertEquals("[1,2,3]", out.toString());
    }

    public void test_4_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        ListSerializer listSerializer = new ListSerializer();
        List<Object> list = new ArrayList<Object>();
        list.add(1L);
        list.add(2L);
        list.add(3L);
        list.add(Collections.emptyMap());
        listSerializer.write(new JSONSerializer(out), list, null, null, 0);

        Assert.assertEquals("[1,2,3,{}]", out.toString());
    }

    public void test_5_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        ListSerializer listSerializer = new ListSerializer();
        List<Object> list = new ArrayList<Object>();
        list.add(1L);
        list.add(21474836480L);
        list.add(null);
        list.add(Collections.emptyMap());
        list.add(21474836480L);
        listSerializer.write(new JSONSerializer(out), list, null, null, 0);

        Assert.assertEquals("[1,21474836480,null,{},21474836480]", out.toString());
    }
}
