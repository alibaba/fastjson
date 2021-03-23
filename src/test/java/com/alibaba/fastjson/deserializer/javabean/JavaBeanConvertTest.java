package com.alibaba.fastjson.deserializer.javabean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import org.junit.Assert;
import org.junit.Test;

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
        paramJson.put("id", 888L);
        paramJson.put("str", "STR");
        paramJson.put("date", "2021-03-23");
        paramJson.put("bool", 1);

        TestDO testDO = TypeUtils.castToJavaBean(paramJson, TestDO.class, ParserConfig.getGlobalInstance());
        Assert.assertNotNull(testDO);
    }

}
