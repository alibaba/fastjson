package com.alibaba.json.bvt.parser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import junit.framework.TestCase;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;

@SuppressWarnings("deprecation")
public class DefaultExtJSONParser_parseArray_2 extends TestCase {

	public void test_0() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[['1']]");
        parser.config(Feature.AllowISO8601DateFormat, false);
        List<List<Integer>> list = (List<List<Integer>>) parser.parseArrayWithType(new TypeReference<List<List<Integer>>>() {
        }.getType());
        Assert.assertEquals(new Integer(1), list.get(0).get(0));
    }

    public void test_1() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("['1','2']");
        parser.config(Feature.AllowISO8601DateFormat, false);
        List<Object> list = new ArrayList<Object>();
        parser.parseArray(Integer.class, list);
        Assert.assertEquals(new Integer(1), list.get(0));
        Assert.assertEquals(new Integer(2), list.get(1));
    }

    public void test_error_0() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("['1','2'}");
        parser.config(Feature.AllowISO8601DateFormat, false);

        Exception error = null;
        try {
            List<Object> list = new ArrayList<Object>();
            parser.parseArray(Integer.class, list);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[['1']]");
        parser.config(Feature.AllowISO8601DateFormat, false);

        Exception error = null;
        try {
            parser.parseArrayWithType(new TypeReference<Map<?, ?>>() {
            }.getType());
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[new X()]");
        parser.config(Feature.AllowISO8601DateFormat, false);

        List list = new ArrayList();
        Exception error = null;
        try {
            parser.parseArray(list);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[] a");
        parser.config(Feature.AllowISO8601DateFormat, false);

        List list = new ArrayList();
        Exception error = null;
        try {
            parser.parseArray(list);
            parser.close();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_4() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("['1','2'}");
        parser.config(Feature.AllowISO8601DateFormat, false);

        Exception error = null;
        try {
            parser.parseArray(new Type[] {});
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_5() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[]");
        parser.config(Feature.AllowISO8601DateFormat, false);

        Assert.assertEquals(1, parser.parseArray(new Type[] { Integer[].class }).length);
    }
    
    public void test_error_6() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("['1' 1 '2'}");
        parser.config(Feature.AllowISO8601DateFormat, false);

        Exception error = null;
        try {
            parser.parseArray(new Type[] {Integer.class});
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
