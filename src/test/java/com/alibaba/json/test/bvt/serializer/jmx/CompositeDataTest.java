package com.alibaba.json.test.bvt.serializer.jmx;

import java.util.HashMap;
import java.util.Map;

import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class CompositeDataTest extends TestCase {

    public void testCompositeData() throws Exception {
        String[] itemNames = { "id", "name" };
        String[] itemDescriptions = itemNames;
        OpenType<?>[] itemTypes = new OpenType[] { SimpleType.INTEGER, SimpleType.STRING };
        CompositeType compositeType = new CompositeType("TypeName", "description", itemNames, itemDescriptions, itemTypes);

        Map<String, Object> items = new HashMap<String, Object>();
        items.put("id", 123);
        items.put("name", "加大伟");

        CompositeDataSupport data = new CompositeDataSupport(compositeType, items);

        JSONObject json = JSON.parseObject(JSON.toJSONString(data));
        Assert.assertEquals(123, json.get("id"));
        Assert.assertEquals("加大伟", json.get("name"));
    }

    public void testCompositeData_null() throws Exception {
        String[] itemNames = { "id", "name" };
        String[] itemDescriptions = itemNames;
        OpenType<?>[] itemTypes = new OpenType[] { SimpleType.INTEGER, SimpleType.STRING };
        CompositeType compositeType = new CompositeType("TypeName", "description", itemNames, itemDescriptions, itemTypes);

        Map<String, Object> items = new HashMap<String, Object>();
        items.put("id", 123);
        items.put("name", null);

        CompositeDataSupport data = new CompositeDataSupport(compositeType, items);

        JSONObject json = JSON.parseObject(JSON.toJSONString(data));
        Assert.assertEquals(123, json.get("id"));

        Assert.assertEquals("{\"id\":123}", JSON.toJSONString(data));
        Assert.assertEquals("{\"id\":123,\"name\":null}", JSON.toJSONString(data, SerializerFeature.WriteMapNullValue));
        Assert.assertEquals("{'id':123}", toJSONString(data, SerializerFeature.QuoteFieldNames, SerializerFeature.UseSingleQuotes));
        Assert.assertEquals("{id:123}", toJSONString(data, SerializerFeature.UseSingleQuotes));
        Assert.assertEquals("{id:123}", toJSONString(data));
    }

    public static final String toJSONString(Object object, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter(features);

        try {
            JSONSerializer serializer = new JSONSerializer(out);

            serializer.write(object);

            return out.toString();
        } catch (StackOverflowError e) {
            throw new JSONException("maybe circular references", e);
        } finally {
            out.close();
        }
    }
}
