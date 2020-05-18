package com.alibaba.json.bvt.serializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.AbstractCollection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONStreamAware;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JSONSerializerTest extends TestCase {

    public void test_0() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new C());

        Assert.assertEquals("[]", serializer.getWriter().toString());
    }

    public void test_0_s() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new C());

        Assert.assertEquals("[]", serializer.getWriter().toString());
    }

    public void test_1() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(Collections.singletonList(1));

        Assert.assertEquals("[1]", serializer.getWriter().toString());
    }

    public void test_1_s() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(Collections.singletonList(1));

        Assert.assertEquals("[1]", serializer.getWriter().toString());
    }

    public void test_2() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(Collections.EMPTY_MAP);

        Assert.assertEquals("{}", serializer.getWriter().toString());
    }

    public void test_2_s() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(Collections.EMPTY_MAP);

        Assert.assertEquals("{}", serializer.getWriter().toString());
    }

    public void test_3() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new JSONAware() {

            public String toJSONString() {
                return "null";
            }
        });

        Assert.assertEquals("null", serializer.getWriter().toString());
    }

    public void test_3_s() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new JSONAware() {

            public String toJSONString() {
                return "null";
            }
        });

        Assert.assertEquals("null", serializer.getWriter().toString());
    }

    public void test_4() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new JSONStreamAware() {

            public void writeJSONString(Appendable out) throws IOException {
                out.append("abc");
            }
        });

        Assert.assertEquals("abc", serializer.getWriter().toString());
    }

    public void test_error() throws Exception {
        JSONException error = null;
        try {
            StringWriter out = new StringWriter();

            JSONSerializer serializer = new JSONSerializer();
            serializer.write(new JSONStreamAware() {

                public void writeJSONString(Appendable out) throws IOException {
                    throw new IOException();
                }
            });
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_5() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new A(3));

        Assert.assertEquals("{\"id\":3}", serializer.getWriter().toString());
    }

    public void test_5_null() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.config(SerializerFeature.WriteMapNullValue, true);
        serializer.write(new A(null));

        Assert.assertEquals("{\"id\":null}", serializer.getWriter().toString());
    }

    public void test_6() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new Date(1293805405498L));

        Assert.assertEquals("1293805405498", serializer.getWriter().toString());
    }

    public void test_7() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new B(1293805405498L));

        Assert.assertEquals("{\"d\":1293805405498}", serializer.getWriter().toString());
    }

    public void test_8() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new B());

        Assert.assertEquals("{}", serializer.getWriter().toString());
    }

    public void test_9() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new D(3L));

        Assert.assertEquals("{\"id\":3}", serializer.getWriter().toString());
    }

    public void test_9_null() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.config(SerializerFeature.WriteMapNullValue, true);
        serializer.write(new D(null));

        Assert.assertEquals("{\"id\":null}", serializer.getWriter().toString());
    }

    public void test_10() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(3);

        Assert.assertEquals("3", serializer.getWriter().toString());
    }

    public void test_11() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(3L);

        Assert.assertEquals("3", serializer.getWriter().toString());
    }

    public void test_12() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new Object[0]);

        Assert.assertEquals("[]", serializer.getWriter().toString());
    }

    public void test_13() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new Object[] { 1 });

        Assert.assertEquals("[1]", serializer.getWriter().toString());
    }

    public void test_14() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new Object[] { 1, 2, 3, 4 });

        Assert.assertEquals("[1,2,3,4]", serializer.getWriter().toString());
    }

    public void test_15() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new Object[] { 1L, 2L, 3L, 4L });

        Assert.assertEquals("[1,2,3,4]", serializer.getWriter().toString());
    }

    public void test_16() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new Object[] { "", "", "", "" });

        Assert.assertEquals("[\"\",\"\",\"\",\"\"]", serializer.getWriter().toString());
    }

    public void test_17() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new Object[] { null, null, null, null });

        Assert.assertEquals("[null,null,null,null]", serializer.getWriter().toString());
    }

    public static class A {

        private Integer id;

        public A(Integer id){
            super();
            this.id = id;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    public static class B {

        private Date d;

        public B(){

        }

        public B(long value){
            super();
            this.d = new Date(value);
        }

        public Date getD() {
            return d;
        }

        public void setD(Date d) {
            this.d = d;
        }

    }

    public static class D {

        private Long id;

        public D(Long id){
            super();
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    public static class C extends AbstractCollection {

        @Override
        public Iterator iterator() {
            return Collections.EMPTY_LIST.iterator();
        }

        @Override
        public int size() {
            return 0;
        }
    }

}
