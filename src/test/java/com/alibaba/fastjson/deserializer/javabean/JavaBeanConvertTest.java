package com.alibaba.fastjson.deserializer.javabean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ylyue
 * @since 2021/3/23
 */
public class JavaBeanConvertTest {

    @Test
    public void javaBeanDeserializerTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("aaa", 1);
        jsonObject.put("bbb", 2);
        jsonObject.put("ccc", "11111");

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);

        JSONObject paramJson = new JSONObject();
        paramJson.put("jsonObject", jsonObject.toJSONString());
        paramJson.put("jsonArray", jsonArray.toJSONString());
        paramJson.put("jsonObjectList", jsonArray.toJSONString());
        Map<String, Object> map = new HashMap();
        map.put("key1", "value1");
        map.put("key2", "value2");
        paramJson.put("map", map);
        paramJson.put("str", "STR");
        paramJson.put("date", "2021-03-23");
        paramJson.put("testEnum", "A_A");
        paramJson.put("inta", "1");
        paramJson.put("intb", "2");
        paramJson.put("longa", "3");
        paramJson.put("longb", 888l);
        paramJson.put("booleana", "1");
        paramJson.put("booleanb", true);
        paramJson.put("arrayStr", new String[]{"aaaa", "bbbbb", "cccc"});
        paramJson.put("arrayLong", new Long[]{1L, 2L, 3L});
        paramJson.put("list", new String[]{"aaaa", "bbbbb", "cccc"});
        paramJson.put("dateTime", "2021-03-24");

        TestDO testDO = TypeUtils.castToJavaBean(paramJson, TestDO.class, ParserConfig.getGlobalInstance());
//        System.out.println(testDO);
        Assert.assertNotNull(testDO);
    }

}
