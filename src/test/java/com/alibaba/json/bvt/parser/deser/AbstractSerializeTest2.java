package com.alibaba.json.bvt.parser.deser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;

public class AbstractSerializeTest2 extends TestCase {

    protected void setUp() throws Exception {
        ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.AbstractSerializeTest2");
        ParserConfig.global.addAccept("com.alibaba.json.bvt.parser.deser.AbstractSerializeTest2");
    }

    protected void tearDown() throws Exception {
        ParserConfig.getGlobalInstance().putDeserializer(A.class, null);
    }

    public void test_mapping_0() throws Exception {
        String text = "{\"@type\":\"com.alibaba.json.bvt.parser.deser.AbstractSerializeTest2$A\"}";

        B b = (B) JSON.parse(text);
        Assert.assertNotNull(b);
    }

    public void test_mapping_1() throws Exception {
        String text = "{\"@type\":\"com.alibaba.json.bvt.parser.deser.AbstractSerializeTest2$A\",\"id\":123}";

        B b = (B) JSON.parse(text);
        Assert.assertNotNull(b);
        Assert.assertEquals(123, b.getId());
    }

    public void test_mapping_2() throws Exception {
        String text = "{\"@type\":\"com.alibaba.json.bvt.parser.deser.AbstractSerializeTest2$A\",\"id\":234,\"name\":\"abc\"}";

        B b = (B) JSON.parse(text);
        Assert.assertNotNull(b);
        Assert.assertEquals(234, b.getId());
        Assert.assertEquals("abc", b.getName());
    }

    public void test_mapping_group() throws Exception {
        String text = "{\"a\":{\"id\":234,\"name\":\"abc\"}}";

        G g = JSON.parseObject(text, G.class);
        Assert.assertTrue(g.getA() instanceof B);
    }

    public static class G {

        private A a;

        public A getA() {
            return a;
        }

        public void setA(A a) {
            this.a = a;
        }

    }

    @JSONType(mappingTo = B.class)
    public static abstract class A {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class B extends A {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
