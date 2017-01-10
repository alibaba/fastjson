package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.MapSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by wenshao on 10/01/2017.
 */
public class Issue989 extends TestCase {

    public void test_for_issue() throws Exception {
        assertEquals(
                JSON.toJSONString(getMyObject(new HashMap<String, Name>())),
                JSON.toJSONString(getMyObject(new TreeMap<String, Name>()))
        );
    }

    private static MyObject getMyObject(Map<String, Name> names) {
        MyObject mapObj = new MyObject();
        mapObj.setNames(names);
        Name name = new Name();
        name.setFirst("foo");
        name.setSecond("boo");
        names.put("mock", name);
        return mapObj;
    }

    public static class NameMapCodec implements ObjectSerializer {

        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
            JSONObject names = new JSONObject();
            for(Map.Entry<String, Name> entry :
                    ((Map<String, Name>)object).entrySet()) {
                Name name = entry.getValue();
                names.put(entry.getKey(), name.getFirst() + ":" + name.getSecond());
            }
            MapSerializer.instance.write(serializer, names, fieldName, JSONObject.class, features);
        }

    }

    public static class MyObject {

        @JSONField(serializeUsing = NameMapCodec.class)
        private Map<String, Name> names;

        public Map<String, Name> getNames() {
            return names;
        }

        public void setNames(Map<String, Name> names) {
            this.names = names;
        }

    }

    private static class Name {

        private String first;

        private String second;

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }
    }
}
