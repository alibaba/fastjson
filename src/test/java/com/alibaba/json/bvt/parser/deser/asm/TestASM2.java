package com.alibaba.json.bvt.parser.deser.asm;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class TestASM2 extends TestCase {

    public void test_0() throws Exception {
        String text = JSON.toJSONString(new V0());
        Assert.assertEquals("{}", text);
    }

    public void test_1() throws Exception {
        String text = JSON.toJSONString(new V1());
        Assert.assertEquals("{\"list\":[]}", text);
    }

    public void test_2() throws Exception {
        V1 v = new V1();
        v.getList().add(3);
        v.getList().add(4);
        String text = JSON.toJSONString(v);
        Assert.assertEquals("{\"list\":[3,4]}", text);
    }

    public void test_3() throws Exception {
        V2 v = new V2();
        v.setId(123);
        v.setName("刘加大");
        String text = JSON.toJSONString(v);
        Assert.assertEquals("{\"id\":123,\"name\":\"刘加大\"}", text);
    }

    public void test_4() throws Exception {
        V2 v = new V2();
        v.setId(123);
        String text = JSON.toJSONString(v);
        Assert.assertEquals("{\"id\":123}", text);
    }

    public void test_7() throws Exception {
        V2 v = new V2();
        v.setId(123);
        String text = JSON.toJSONString(v, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"id\":123,\"name\":null}", text);
    }

    public void test_8() throws Exception {
        V3 v = new V3();
        v.setText("xxx");
        String text = JSON.toJSONString(v, SerializerFeature.UseSingleQuotes);
        Assert.assertEquals("{'text':'xxx'}", text);
    }

    public void test_9() throws Exception {
        V3 v = new V3();
        v.setText("xxx");
        String text = JSON.toJSONString(v, SerializerFeature.UseSingleQuotes, SerializerFeature.WriteMapNullValue);
        System.out.println(text);
        
        Assert.assertEquals(true, "{'list':null,'text':'xxx'}".equals(text) || "{'text':'xxx','list':null}".equals(text));
        
    }

    public void f_test_3() throws Exception {
        V1 v = new V1();
        v.getList().add(3);
        String text = JSON.toJSONString(v, SerializerFeature.UseSingleQuotes);
        System.out.println(text);
    }

    public static class V0 {

    }

    public static class V1 {

        private List<Integer> list = new ArrayList<Integer>();

        public List<Integer> getList() {
            return list;
        }

        public void setList(List<Integer> list) {
            this.list = list;
        }

    }

    public static class V2 {

        private int    id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class V3 {

        private List<Integer> list;
        private String        text;

        public List<Integer> getList() {
            return list;
        }

        public void setList(List<Integer> list) {
            this.list = list;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

    }

}
