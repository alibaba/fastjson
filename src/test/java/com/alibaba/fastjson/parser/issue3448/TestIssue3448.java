package com.alibaba.fastjson.parser.issue3448;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author wuzihao
 * @date 2020/9/17
 * https://github.com/alibaba/fastjson/issues/3448
 */
public class TestIssue3448 {

    public static class SelfTypeReference<T> {

    }

    @Test
    public void test() {
        List<Map<String, List<String>>> list = new ArrayList<>(4);
        list.add(Collections.singletonMap("key1", Collections.singletonList("item")));
        String text = JSON.toJSONString(list);
        System.out.println("text = " + text);

        List<Map<String, List<String>>> result = parseObject(text,
                new TestIssue3448.SelfTypeReference<Map<String, List<String>>>() {
                });
        System.out.println("result = " + result);
    }

    @Test
    public void test2() {
        List<List<String>> list = new ArrayList<>(4);
        list.add(Collections.singletonList("item"));
        String text = JSON.toJSONString(list);
        System.out.println("text = " + text);

        List<List<String>> result = parseObject(text,
                new TestIssue3448.SelfTypeReference<List<String>>() {
                });
        System.out.println("result = " + result);
    }

    @Test
    public void test3() {
        List<String> list = new ArrayList<>(4);
        list.add("item");
        String text = JSON.toJSONString(list);
        System.out.println("text = " + text);

        List<String> result = parseObject(text,
                new TestIssue3448.SelfTypeReference<String>() {
                });
        System.out.println("result = " + result);
    }

    public <T> List<T> parseObject(String text, TestIssue3448.SelfTypeReference<T> selfTypeReference) {
        Type genericSuperclass = selfTypeReference.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        return JSON.parseObject(text, new TypeReference<List<T>>(actualTypeArguments) {
        });
    }


}
