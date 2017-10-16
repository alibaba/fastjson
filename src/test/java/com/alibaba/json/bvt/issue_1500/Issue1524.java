package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import junit.framework.TestCase;

import java.io.IOException;
import java.lang.reflect.Type;

public class Issue1524 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.oldValue = new Value();

        String json = JSON.toJSONString(model, new NameFilter() {
            public String process(Object object, String name, Object value) {
                if ("oldValue".equals(name)) {
                    return "old_value";
                }
                return name;
            }
        });
        System.out.println(json);
    }

    public static class Model {
        @JSONField(serializeUsing = ValueSerializer.class)
        public Value oldValue;
    }

    public static class Value {

    }

    public static class ValueSerializer implements ObjectSerializer {

        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
            serializer.write("xx");
        }
    }
}
