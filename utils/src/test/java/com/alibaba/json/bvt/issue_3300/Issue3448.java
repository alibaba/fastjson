package com.alibaba.json.bvt.issue_3300;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author yumin.pym
 */
public class Issue3448 extends TestCase {
    public static class SelfTypeReference<T> {

    }

    @Test
    public void test() {
        List<Map<String, List<String>>> list = new ArrayList(4);
        list.add(Collections.singletonMap("key1", Collections.singletonList("item")));
        String text = JSON.toJSONString(list);
        System.out.println("text = " + text);

        List<Map<String, List<String>>> result = parseObject(text,
            new SelfTypeReference<Map<String, List<String>>>() {});
        System.out.println("result = " + result);
        TestCase.assertTrue(result.get(0) instanceof Map);
        TestCase.assertTrue(result.get(0).get("key1").get(0) instanceof String);
    }

    public <T> List<T> parseObject(String text, SelfTypeReference<T> selfTypeReference) {
        Type genericSuperclass = selfTypeReference.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType)genericSuperclass).getActualTypeArguments();
        return JSON.parseObject(text, new TypeReference<List<T>>(actualTypeArguments) {});
    }

    @Test
    public void test_1() {
        List<Map<String, List<String>>> list = new ArrayList(4);
        list.add(Collections.singletonMap("key1", Collections.singletonList("item")));
        String text = JSON.toJSONString(list);
        System.out.println("text = " + text);

        List<Map<String, List<String>>> result = parseObject2(text,
                new SelfTypeReference<Map<String, List<String>>>() {
                });
        System.out.println("result = " + result);
    }

    @Test
    public void test2() {
        List<List<String>> list = new ArrayList(4);
        list.add(Collections.singletonList("item"));
        String text = JSON.toJSONString(list);
        System.out.println("text = " + text);

        List<List<String>> result = parseObject2(text,
                new SelfTypeReference<List<String>>() {
                });
        System.out.println("result = " + result);
    }

    @Test
    public void test3() {
        List<String> list = new ArrayList(4);
        list.add("item");
        String text = JSON.toJSONString(list);
        System.out.println("text = " + text);

        List<String> result = parseObject2(text,
                new SelfTypeReference<String>() {
                });
        System.out.println("result = " + result);
    }

    public <T> List<T> parseObject2(String text, SelfTypeReference<T> selfTypeReference) {
        Type genericSuperclass = selfTypeReference.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        return JSON.parseObject(text, new TypeReference<List<T>>(actualTypeArguments) {
        });
    }
}
