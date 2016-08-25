package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;

import static com.alibaba.fastjson.serializer.ASMSerializerFactory.Context.features;

/**
 * Created with IntelliJ IDEA.
 * User: jarvan4dev
 * Date: 16/8/25
 * Time: 下午1:12
 */
public class TestWithoutSortField {

    public static void main(String[] args) {
        User user = new User();
        user.setIdNo("10086");
        user.setUserName("jarvan4dev");
        user.setPasswordStr("123456");
        user.setAddrDetail("上海市浦东新区");
        user.setAge(25);
        user.setGender("男");
        user.setSchool("上海大学");
        NameFilter filter = new NameFilter() {
            @Override
            public String process(Object object, String name, Object value) {
                return name.toUpperCase();
            }
        };
        SerializerFeature.config(features, SerializerFeature.SortField, false);
        String json = JSON.toJSONString(user);
        System.out.println(json);
    }
}
