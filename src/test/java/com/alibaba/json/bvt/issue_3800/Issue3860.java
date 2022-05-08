package com.alibaba.json.bvt.issue_3800;

import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.IdentityHashMap;

import junit.framework.TestCase;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

public class Issue3860 extends TestCase {
    public void test_for_issue() throws Exception {
        ParserConfig config = new ParserConfig();
        IdentityHashMap<Type, ObjectDeserializer> deserializers = config.getDeserializers();
        int initSize = deserializers.size();
        for (int i = 0; i < 1000 * 10; ++i) {
            assertEquals(123,
                ((VO<User>) JSON.parseObject("{\"value\":{\"id\":123}}",
                    ParameterizedTypeImpl.make(VO.class, new Type[] {User.class}, null),
                        config
                        )).value.id
            );
        }

        assertEquals(2, deserializers.size() - initSize);
    }

    public static class VO<T> {
        public T value;
    }

    public static class User {
        public int id;
    }
}
