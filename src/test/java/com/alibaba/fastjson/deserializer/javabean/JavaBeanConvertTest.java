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
 * JavaBean类型转换器测试
 *
 * @author ylyue
 * @since 2021/3/23
 */
public class JavaBeanConvertTest {

    @Test
    public void javaBeanDeserializerTest() {
        Map<String, Object> map = new HashMap();
        map.put("key1", "value1");
        map.put("key2", "value2");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("aaa", 1);
        jsonObject.put("bbb", 2);
        jsonObject.put("ccc", "11111");

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);

        JSONObject paramJson = new JSONObject();
        // JSON - JSON
        paramJson.put("map", map);
        paramJson.put("jsonObject", jsonObject);
        paramJson.put("jsonArray", jsonArray);
        paramJson.put("jsonObjectList", jsonArray);
        // JSONString - JSON
        paramJson.put("strToMap", map);
        paramJson.put("strToJsonObject", jsonObject.toJSONString());
        paramJson.put("strToJsonArray", jsonArray.toJSONString());
        paramJson.put("strToJsonObjectList", jsonArray.toJSONString());

        // 基本类型
        paramJson.put("character", "c");
        paramJson.put("str", "STR");
        paramJson.put("inta", "1");
        paramJson.put("intb", "2");
        paramJson.put("longa", "3");
        paramJson.put("longb", 888l);
        paramJson.put("booleana", "1");
        paramJson.put("booleanb", true);

        // 数组
        paramJson.put("arrayStr", new String[]{"aaaa", "bbbbb", "cccc"});
        paramJson.put("arrayLong", new Long[]{1L, 2L, 3L});
        paramJson.put("list", new String[]{"aaaa", "bbbbb", "cccc"});

        // 时间类型
        paramJson.put("date", "2021-03-23");
        paramJson.put("dateTime", "2021-03-24");
        paramJson.put("localDate", "2021-03-24");
        paramJson.put("localTime", "16:03:24");
        paramJson.put("localDateTime", "2021-03-24 16:03:24");

        // 其它
        paramJson.put("convertEnum", "A_A");

        ConvertDO convertDO = TypeUtils.castToJavaBean(paramJson, ConvertDO.class, ParserConfig.getGlobalInstance());
        Assert.assertNotNull(convertDO);
//        System.out.println(convertDO);
    }

}
