package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.serializer.DelayObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class PropertyFilter_double extends TestCase {

    public void test_0() throws Exception {
        PropertyFilter filter = new PropertyFilter() {

            public boolean apply(Object source, String name, DelayObject value) {
                return false;
            }
        };

        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.getPropertyFilters().add(filter);

        A a = new A();
        serializer.write(a);

        String text = out.toString();
        Assert.assertEquals("{}", text);
    }

    public void test_1() throws Exception {
        PropertyFilter filter = new PropertyFilter() {

            public boolean apply(Object source, String name, DelayObject value) {
                if ("id".equals(name)) {
					Object v = value.getValue();
                    Assert.assertTrue(v instanceof Double);
                    return true;
                }
                return false;
            }
        };

        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.getPropertyFilters().add(filter);

        A a = new A();
        serializer.write(a);

        String text = out.toString();
        Assert.assertEquals("{\"id\":0}", text);
    }

    public void test_2() throws Exception {
        PropertyFilter filter = new PropertyFilter() {

            public boolean apply(Object source, String name, DelayObject value) {
                if ("name".equals(name)) {
                    return true;
                }
                return false;
            }
        };

        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.getPropertyFilters().add(filter);

        A a = new A();
        a.setName("chennp2008");
        serializer.write(a);

        String text = out.toString();
        Assert.assertEquals("{\"name\":\"chennp2008\"}", text);
    }

    public void test_3() throws Exception {
        PropertyFilter filter = new PropertyFilter() {

            public boolean apply(Object source, String name, DelayObject value) {
                if ("name".equals(name)) {
                    return true;
                }
                return false;
            }
        };

        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.getPropertyFilters().add(filter);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "chennp2008");
        serializer.write(map);

        String text = out.toString();
        Assert.assertEquals("{\"name\":\"chennp2008\"}", text);
    }

    public void test_4() throws Exception {
        PropertyFilter filter = new PropertyFilter() {

            public boolean apply(Object source, String name, DelayObject value) {
                if ("name".equals(name)) {
                    return false;
                }
                return true;
            }
        };

        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.getPropertyFilters().add(filter);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 3);
        map.put("name", "chennp2008");
        serializer.write(map);

        String text = out.toString();
        Assert.assertEquals("{\"id\":3}", text);
    }

    public static class A {

        private double    id;
        private String name;

        public double getId() {
            return id;
        }

        public void setId(double id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
