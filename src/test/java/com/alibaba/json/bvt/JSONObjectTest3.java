package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;

public class JSONObjectTest3 extends TestCase {

    public void test_0() throws Exception {
        String text = "{value:'123',big:false}";
        Bean bean = JSON.parseObject(text, Bean.class);
        Assert.assertEquals("123", bean.getValue());
        Assert.assertEquals(false, bean.isBig());
        Assert.assertEquals(123, bean.getIntValue());

        bean.setBig(true);
        Assert.assertEquals(true, bean.isBig());

        bean.setID(567);
        Assert.assertEquals(567, bean.getID());

    }

    public void test_error_0() throws Exception {
        String text = "{value:'123',big:false}";
        Bean bean = JSON.parseObject(text, Bean.class);

        JSONException error = null;
        try {
            bean.f();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() throws Exception {
        String text = "{value:'123',big:false}";
        Bean bean = JSON.parseObject(text, Bean.class);

        JSONException error = null;
        try {
            bean.f(1);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() throws Exception {
        String text = "{value:'123',big:false}";
        Bean bean = JSON.parseObject(text, Bean.class);

        JSONException error = null;
        try {
            bean.get();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() throws Exception {
        String text = "{value:'123',big:false}";
        Bean bean = JSON.parseObject(text, Bean.class);

        JSONException error = null;
        try {
            bean.is();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_4() throws Exception {
        String text = "{value:'123',big:false}";
        Bean bean = JSON.parseObject(text, Bean.class);

        Exception error = null;
        try {
            bean.f(1, 2);
        } catch (UnsupportedOperationException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_5() throws Exception {
        String text = "{value:'123',big:false}";
        Bean bean = JSON.parseObject(text, Bean.class);

        JSONException error = null;
        try {
            bean.getA();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_6() throws Exception {
        String text = "{value:'123',big:false}";
        Bean bean = JSON.parseObject(text, Bean.class);

        JSONException error = null;
        try {
            bean.f1(1);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_7() throws Exception {
        String text = "{value:'123',big:false}";
        Bean bean = JSON.parseObject(text, Bean.class);

        JSONException error = null;
        try {
            bean.set(1);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_8() throws Exception {
        String text = "{value:'123',big:false}";
        Bean bean = JSON.parseObject(text, Bean.class);

        JSONException error = null;
        try {
            bean.xx();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static interface Bean {

        String getValue();

        void setValue(String value);

        boolean isBig();

        @JSONField
        void setBig(boolean value);

        @JSONField(name = "value")
        int getIntValue();

        @JSONField(name = "id")
        void setID(int value);

        @JSONField(name = "id")
        int getID();

        Object get();

        Object xx();

        void set(int i);

        boolean is();

        void getA();

        void f();

        Object f(int a);

        void f1(int a);

        void f(int a, int b);
    }
}
