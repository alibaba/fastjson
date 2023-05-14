package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.annotation.JSONField;
import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;

public class MapTest extends TestCase {

    public void test_no_sort() throws Exception {
        JSONObject obj = new JSONObject(true);
        obj.put("name", "jobs");
        obj.put("id", 33);
        String text = toJSONString(obj);
        Assert.assertEquals("{'name':'jobs','id':33}", text);
    }
    
    public void test_null() throws Exception {
        JSONObject obj = new JSONObject(true);
        obj.put("name", null);
        String text = JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"name\":null}", text);
    }

    public static final String toJSONString(Object object) {
        SerializeWriter out = new SerializeWriter();

        try {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.config(SerializerFeature.SortField, false);
            serializer.config(SerializerFeature.UseSingleQuotes, true);

            serializer.write(object);

            return out.toString();
        } catch (StackOverflowError e) {
            throw new JSONException("maybe circular references", e);
        } finally {
            out.close();
        }
    }

    public void test_onJSONField() {
        Map<String, String> map = new HashMap();
        map.put("Ariston", null);
        MapNullValue mapNullValue = new MapNullValue();
        mapNullValue.setMap( map );
        String json = JSON.toJSONString( mapNullValue );
        assertEquals("{\"map\":{\"Ariston\":null}}", json);
    }

    class MapNullValue {
        @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue})
        private Map map;

        public Map getMap() {
            return map;
        }

        public void setMap( Map map ) {
            this.map = map;
        }
    }

}
