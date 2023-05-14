package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

public class Issue1513 extends TestCase {
    public void test_for_issue() throws Exception {
        {
            Model<Object> model = JSON.parseObject("{\"values\":[{\"id\":123}]}", new TypeReference<Model<Object>>(){});
            assertNotNull(model.values);
            assertEquals(1, model.values.length);
            JSONObject object = (JSONObject) model.values[0];
            assertEquals(123, object.getIntValue("id"));
        }
        {
            Model<A> model = JSON.parseObject("{\"values\":[{\"id\":123}]}", new TypeReference<Model<A>>(){});
            assertNotNull(model.values);
            assertEquals(1, model.values.length);
            A a = model.values[0];
            assertEquals(123, a.id);
        }
        {
            Model<B> model = JSON.parseObject("{\"values\":[{\"value\":123}]}", new TypeReference<Model<B>>(){});
            assertNotNull(model.values);
            assertEquals(1, model.values.length);
            B b = model.values[0];
            assertEquals(123, b.value);
        }
        {
            Model<C> model = JSON.parseObject("{\"values\":[{\"age\":123}]}", new TypeReference<Model<C>>(){});
            assertNotNull(model.values);
            assertEquals(1, model.values.length);
            C c = model.values[0];
            assertEquals(123, c.age);
        }
    }

    public static class Model<T> {
        public T[] values;
    }

    public static class A {
        public int id;
    }

    public static class B {
        public int value;
    }

    public static class C {
        public int age;
    }
}
