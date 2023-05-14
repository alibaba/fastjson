package com.alibaba.json.bvt.serializer.enum_;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import junit.framework.TestCase;

import java.lang.reflect.Type;

public class EnumCustomCodecTest extends TestCase {

    public void test_for_enum() throws Exception {
        Type type = Type.A;
        String str =JSON.toJSONString(type);

        assertEquals("{\"id\":1001,\"name\":\"aaa\"}", str);

        Type type2 = JSON.parseObject(str, Type.class);
        Type type3 = JSON.parseObject(str, Type.class);

        assertSame(type, type2);
        assertSame(type, type3);

    }

    @JSONType(serializeEnumAsJavaBean = true, deserializer = TypeDeser.class)
    public static enum Type {
        A(1001, "aaa"), B(1002, "bbb")
        ;

        public int id;
        public String name;

        Type(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public static Type valueOf(long id) {
            if (id == 1001) {
                return A;
            } else if (id == 1002) {
                return B;
            }
            return null;
        }
    }

    public static class TypeDeser implements ObjectDeserializer {

        public <T> T deserialze(DefaultJSONParser parser, java.lang.reflect.Type type, Object fieldName) {
            JSONObject object = parser.parseObject();
            long id = object.getLongValue("id");
            return (T) Type.valueOf(id);
        }

        public int getFastMatchToken() {
            return 0;
        }
    }
}
