
package com.alibaba.json.bvt.issue_3600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Issue3623 {
    private static final String str = "abcd";

    @Test
    public void test_for_issue_1() {
        Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();

        map.put(str.getBytes(), str.getBytes());
        byte[] noClassName = JSON.toJSONBytes(map);
        byte[] ClassName = JSON.toJSONBytes(map, SerializerFeature.WriteClassName);
        String strNoClassName = new String(noClassName);
        String strClassName = new String(ClassName);
        System.out.println(strNoClassName);
        System.out.println(strClassName);

        Assert.assertNotNull(JSON.parseObject(noClassName, map.getClass()));
        Assert.assertNotNull(JSON.parseObject(ClassName, map.getClass()));
    }

    @Test
    public void test_for_issue_2() {
        Pojo2 obj = new Pojo2();
        Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
        map.put(str.getBytes(), str.getBytes());
        obj.setValues(new Object[]{map});

        byte[] ser = JSON.toJSONBytes(obj, SerializerFeature.WriteClassName);
        System.out.println(new String(ser));

        Assert.assertNotNull(JSON.parseObject(ser, Pojo2.class));
    }

    @Data
    public static class Pojo2 implements Serializable {
        private Object[] values;
    }
}

