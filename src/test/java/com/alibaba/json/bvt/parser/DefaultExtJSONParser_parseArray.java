package com.alibaba.json.bvt.parser;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONToken;

public class DefaultExtJSONParser_parseArray extends TestCase {

    public void test_0() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[1,2,,,3]");
        List list = new ArrayList();
        parser.parseArray(int.class, list);
        Assert.assertEquals("[1, 2, 3]", list.toString());
    }

    public void test_1() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[1,2,3]");
        parser.config(Feature.AllowArbitraryCommas, true);
        List list = new ArrayList();
        parser.parseArray(int.class, list);
        Assert.assertEquals("[1, 2, 3]", list.toString());
    }

    public void test_2() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("['1','2','3']");
        parser.config(Feature.AllowArbitraryCommas, true);
        List list = new ArrayList();
        parser.parseArray(String.class, list);
        Assert.assertEquals("[1, 2, 3]", list.toString());
        Assert.assertEquals("1", list.get(0));
    }

    public void test_3() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[1,2,3]");
        parser.config(Feature.AllowArbitraryCommas, true);
        List list = new ArrayList();
        parser.parseArray(BigDecimal.class, list);
        Assert.assertEquals("[1, 2, 3]", list.toString());
        Assert.assertEquals(new BigDecimal("1"), list.get(0));
    }

    public void test_4() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[1,2,3,null]");
        parser.config(Feature.AllowArbitraryCommas, true);
        List list = new ArrayList();
        parser.parseArray(BigDecimal.class, list);
        Assert.assertEquals("[1, 2, 3, null]", list.toString());
        Assert.assertEquals(new BigDecimal("1"), list.get(0));
    }

    public void test_5() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[1,2,3,null]");
        Object[] array = parser.parseArray(new Type[] { Integer.class, BigDecimal.class, Long.class, String.class });
        Assert.assertEquals(new Integer(1), array[0]);
        Assert.assertEquals(new BigDecimal("2"), array[1]);
        Assert.assertEquals(new Long(3), array[2]);
        Assert.assertEquals(null, array[3]);
    }

    public void test_error() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{}");
        Exception error = null;
        try {
            parser.parseArray(new ArrayList());
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_6() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[1.2]");
        parser.config(Feature.UseBigDecimal, false);
        ArrayList list = new ArrayList();
        parser.parseArray(list);
        Assert.assertEquals(Double.valueOf(1.2), list.get(0));
    }

    public void test_7() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[\"2011-01-09T13:49:53.254\", \"xxx\", true, false, null, {}]");
        parser.config(Feature.AllowISO8601DateFormat, true);
        ArrayList list = new ArrayList();
        parser.parseArray(list);
        Assert.assertEquals(new Date(1294552193254L), list.get(0));
        Assert.assertEquals("xxx", list.get(1));
        Assert.assertEquals(Boolean.TRUE, list.get(2));
        Assert.assertEquals(Boolean.FALSE, list.get(3));
        Assert.assertEquals(null, list.get(4));
        Assert.assertEquals(new JSONObject(), list.get(5));
    }

    public void test_8() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("\"2011-01-09T13:49:53.254\"");
        parser.config(Feature.AllowISO8601DateFormat, true);
        Object value = parser.parse();
        Assert.assertEquals(new Date(1294552193254L), value);
    }

    public void test_9() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("");
        parser.config(Feature.AllowISO8601DateFormat, true);
        Object value = parser.parse();
        Assert.assertEquals(null, value);
    }

    public void test_error_2() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{}");
        Exception error = null;
        try {
            parser.accept(JSONToken.NULL);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_10() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[1,2,3]");
        Object[] array = parser.parseArray(new Type[] { Integer[].class });
        Integer[] values = (Integer[]) array[0];
        Assert.assertEquals(new Integer(1), values[0]);
        Assert.assertEquals(new Integer(2), values[1]);
        Assert.assertEquals(new Integer(3), values[2]);
    }

    public void test_11() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[1]");
        Object[] array = parser.parseArray(new Type[] { String.class });
        Assert.assertEquals("1", array[0]);
    }

    public void test_12() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("['1']");
        Object[] array = parser.parseArray(new Type[] { int.class });
        Assert.assertEquals(new Integer(1), array[0]);
    }

    public void test_13() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("['1']");
        Object[] array = parser.parseArray(new Type[] { Integer.class });
        Assert.assertEquals(new Integer(1), array[0]);
    }

    public void test_14() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[]");
        Object[] array = parser.parseArray(new Type[] {});
        Assert.assertEquals(0, array.length);
    }

    public void test_15() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[1,null]");
        ArrayList list = new ArrayList();
        parser.config(Feature.AllowISO8601DateFormat, false);
        parser.parseArray(String.class, list);
        Assert.assertEquals("1", list.get(0));
        Assert.assertEquals(null, list.get(1));
    }

    public void test_16() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[[1]]");
        parser.config(Feature.AllowISO8601DateFormat, false);
        Object[] array = parser.parseArray(new Type[] { new TypeReference<List<Integer>>() {
        }.getType() });
        Assert.assertEquals(new Integer(1), ((List<Integer>) (array[0])).get(0));
    }

    public void test_17() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[]");
        Object[] array = parser.parseArray(new Type[] { Integer[].class });
        Integer[] values = (Integer[]) array[0];
        Assert.assertEquals(0, values.length);
    }

    public void test_18() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("null");
        parser.config(Feature.AllowISO8601DateFormat, false);
        List<Integer> list = (List<Integer>) parser.parseArrayWithType(new TypeReference<List<Integer>>() {
        }.getType());
        Assert.assertEquals(null, list);
    }

    public void test_error_var() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[1,2,null }");
        parser.config(Feature.AllowISO8601DateFormat, false);

        Exception error = null;
        try {
            Object[] array = parser.parseArray(new Type[] { Integer[].class });
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[1,null }");
        ArrayList list = new ArrayList();
        parser.config(Feature.AllowISO8601DateFormat, false);

        Exception error = null;
        try {
            parser.parseArray(String.class, list);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_4() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[1,null }");
        parser.config(Feature.AllowISO8601DateFormat, false);

        Exception error = null;
        try {
            parser.parseArray(new Type[] { String.class });
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_5() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[1,null }");
        ArrayList list = new ArrayList();
        parser.config(Feature.AllowISO8601DateFormat, false);

        Exception error = null;
        try {
            parser.parseArray(String.class, list);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_6() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{1,null }");
        parser.config(Feature.AllowISO8601DateFormat, false);

        Exception error = null;
        try {
            parser.parseArray(new Type[] { String.class });
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_7() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{1}");
        parser.config(Feature.AllowISO8601DateFormat, false);

        Exception error = null;
        try {
            parser.parseArray(new Type[] {});
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_8() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[1,2,3 4]");
        parser.config(Feature.AllowISO8601DateFormat, false);

        Exception error = null;
        try {
            parser.parseArray(new Type[] { Integer.class });
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
