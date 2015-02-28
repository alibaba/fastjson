package com.alibaba.json.bvt.serializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.FieldSerializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class JavaBeanSerializerTest extends TestCase {

    public void test_0_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        A a = new A();
        a.getL0().add("A");
        a.getL0().add("B");

        JavaBeanSerializer serializer = new JavaBeanSerializer(A.class);
        serializer.write(new JSONSerializer(out), a, null, null, 0);

        Assert.assertEquals("{\"l0\":[\"A\",\"B\"]}", out.toString());
    }

    public void test_1_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        B a = new B();
        a.getL0().add("A");
        a.getL0().add("B");

        JavaBeanSerializer serializer = new JavaBeanSerializer(B.class);
        serializer.write(new JSONSerializer(out), a, null, null, 0);

        Assert.assertEquals("{\"l0\":[\"A\",\"B\"],\"l1\":[]}", out.toString());
    }

    public void test_2_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JavaBeanSerializer serializer = new JavaBeanSerializer(F.class);
        serializer.write(new JSONSerializer(out), new F(new E(123)), null, null, 0);

        Assert.assertEquals("{\"e\":{\"id\":123}}", out.toString());
    }

    public void test_3_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JavaBeanSerializer serializer = new JavaBeanSerializer(F.class);
        serializer.write(new JSONSerializer(out), new F(null), null, null, 0);

        for (FieldSerializer getter : serializer.getGetters()) {
            getter.getName();
            getter.getMethod();
        }

        Assert.assertEquals("{}", out.toString());
    }

    public void test_error_s() throws Exception {
        JSONException error = null;
        try {
            SerializeWriter out = new SerializeWriter();
            JavaBeanSerializer serializer = new JavaBeanSerializer(C.class);
            serializer.write(new JSONSerializer(out), new C(), null, null, 0);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1_s() throws Exception {
        JSONException error = null;
        try {
            SerializeWriter out = new SerializeWriter();
            JavaBeanSerializer serializer = new JavaBeanSerializer(D.class);
            serializer.write(new JSONSerializer(out), new D(), null, null, 0);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public static class A {

        private List<String> l0 = new ArrayList<String>();

        public List<String> getL0() {
            return l0;
        }

        public void setL0(List<String> l0) {
            this.l0 = l0;
        }

        public Object get() {
            return null;
        }

        public Object getx() {
            return null;
        }

        public boolean is() {
            return true;
        }

        public boolean isx() {
            return true;
        }
    }

    public static class B {

        private Collection<String> l0 = new ArrayList<String>();
        private Collection<String> l1 = new ArrayList<String>();

        public Collection<String> getL1() {
            return l1;
        }

        public void setL1(Collection<String> l1) {
            this.l1 = l1;
        }

        public Collection<String> getL0() {
            return l0;
        }

        public void setL0(Collection<String> l0) {
            this.l0 = l0;
        }

        public Object get() {
            return null;
        }

        public Object getx() {
            return null;
        }

        public boolean is() {
            return true;
        }

        public boolean isx() {
            return true;
        }
    }

    public static class C {

        public List<String> getL0() {
            throw new RuntimeException();
        }

        public void setL0(List<String> l0) {
        }
    }

    public static class D {

        public Collection<String> getL0() {
            throw new RuntimeException();
        }
    }

    public static class E {

        private int id;

        public E(){
        }

        public E(int id){
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class F {

        private E e;

        public F(){
        }

        public F(E e){
            this.e = e;
        }

        public E getE() {
            return e;
        }

        public void setE(E e) {
            this.e = e;
        }

    }
}
