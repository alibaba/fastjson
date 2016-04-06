package com.alibaba.json.bvt.parser.deser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

public class ArrayListStringDeserializerTest extends TestCase {
    public void test_null() throws Exception {
        Assert.assertNull(JSON.parseObject("null", new TypeReference<List<String>>() {
        }));
        
        Assert.assertNull(JSON.parseArray("null", new Type[] {new TypeReference<List<String>>() {
        }.getType()}));
        
        Assert.assertNull(JSON.parseArray("null", Entity.class));
        Assert.assertNull(JSON.parseArray("null", Entity[].class));
        Assert.assertNull(JSON.parseArray("null"));
        Assert.assertNull(JSON.parseObject("null"));
        Assert.assertNull(JSON.parseObject("null", Object[].class));
        Assert.assertNull(JSON.parseObject("null", Entity[].class));
        
        Assert.assertNull(JSON.parseArray("[null]", new Type[] {new TypeReference<List<String>>() {
        }.getType()}).get(0));
    }

    public void test_strings() throws Exception {
        Entity a = JSON.parseObject("{units:['NANOSECONDS', 'SECONDS', 3, null]}", Entity.class);
        Assert.assertEquals("NANOSECONDS", a.getUnits().get(0));
        Assert.assertEquals("SECONDS", a.getUnits().get(1));
        Assert.assertEquals("3", a.getUnits().get(2));
        Assert.assertEquals(null, a.getUnits().get(3));

    }

    public void test_strings_() throws Exception {
        Entity a = JSON.parseObject("{units:['NANOSECONDS',,,, 'SECONDS', 3, null]}", Entity.class);
        Assert.assertEquals("NANOSECONDS", a.getUnits().get(0));
        Assert.assertEquals("SECONDS", a.getUnits().get(1));
        Assert.assertEquals("3", a.getUnits().get(2));
        Assert.assertEquals(null, a.getUnits().get(3));

    }

    public void test_strings_2() throws Exception {
        List<String> list = JSON.parseObject("['NANOSECONDS', 'SECONDS', 3, null]", new TypeReference<List<String>>() {
        });
        Assert.assertEquals("NANOSECONDS", list.get(0));
        Assert.assertEquals("SECONDS", list.get(1));
        Assert.assertEquals("3", list.get(2));
        Assert.assertEquals(null, list.get(3));
    }

    public void test_strings_3() throws Exception {
        List<String> list = JSON.parseObject("['NANOSECONDS', 'SECONDS', 3, null]", new TypeReference<List<String>>() {
        }.getType(), 0, Feature.AllowSingleQuotes);
        Assert.assertEquals("NANOSECONDS", list.get(0));
        Assert.assertEquals("SECONDS", list.get(1));
        Assert.assertEquals("3", list.get(2));
        Assert.assertEquals(null, list.get(3));
    }

    public void test_string_error_not_eof() throws Exception {
        JSONException ex = null;
        try {
            JSON.parseObject("[}", new TypeReference<List<String>>() {
            }.getType(), 0, Feature.AllowSingleQuotes);
        } catch (JSONException e) {
            ex = e;
        }
        Assert.assertNotNull(ex);
    }

    public void test_string_error() throws Exception {
        JSONException ex = null;
        try {
            JSON.parseObject("'123'", new TypeReference<List<String>>() {
            });
        } catch (JSONException e) {
            ex = e;
        }
        Assert.assertNotNull(ex);
    }

    public void test_string_error_1() throws Exception {
        JSONException ex = null;
        try {
            parseObject("{units:['NANOSECONDS',,,, 'SECONDS', 3, null]}", Entity.class);
        } catch (JSONException e) {
            ex = e;
        }
        Assert.assertNotNull(ex);
    }

    public static final <T> T parseObject(String input, Type clazz, Feature... features) {
        if (input == null) {
            return null;
        }

        int featureValues = 0;
        for (Feature feature : features) {
            featureValues = Feature.config(featureValues, feature, true);
        }

        DefaultJSONParser parser = new DefaultJSONParser(input, ParserConfig.getGlobalInstance(), featureValues);
        T value = (T) parser.parseObject(clazz);

        if (clazz != JSONArray.class) {
            parser.close();
        }

        return (T) value;
    }

    public static class Entity {

        private List<String> units = new ArrayList<String>();

        public List<String> getUnits() {
            return units;
        }

        public void setUnits(List<String> units) {
            this.units = units;
        }

    }
}
