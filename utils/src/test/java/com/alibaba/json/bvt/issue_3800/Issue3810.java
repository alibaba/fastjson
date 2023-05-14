package com.alibaba.json.bvt.issue_3800;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import junit.framework.TestCase;
import lombok.Data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/alibaba/fastjson/issues/3810
 *
 * @author hnyyghk
 * @date 2021/06/30 18:40
 */
public class Issue3810 extends TestCase {
    @Data
    static class TestA<T> {
        T a;
    }

    @Data
    static class TestB {
        String b;
    }

    private final static String json = "{\"a\":[{\"b\":\"b\"}]}";

    public void test_for_issue() throws Exception {
        ParameterizedTypeImpl inner = new ParameterizedTypeImpl(new Type[]{TestB.class}, List.class, List.class);
        ParameterizedTypeImpl outer = new ParameterizedTypeImpl(new Type[]{inner}, TestA.class, TestA.class);
        JSONObject jo = JSONObject.parseObject(json);
        TestA<List<?>> ret = jo.toJavaObject(outer);

        assertEquals(ArrayList.class.getName(), ret.getA().getClass().getName());
        assertEquals(TestB.class.getName(), ret.getA().get(0).getClass().getName());
    }
}