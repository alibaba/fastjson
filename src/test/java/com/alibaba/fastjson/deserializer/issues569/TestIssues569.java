package com.alibaba.fastjson.deserializer.issues569;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.deserializer.issues569.beans.Dept;
import com.alibaba.fastjson.deserializer.issues569.beans.MyResponse;
import com.alibaba.fastjson.deserializer.issues569.parser.ParserConfigBug569;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-10-10
 *
 * https://github.com/alibaba/fastjson/issues/569
 */
public class TestIssues569 {

    private int featureValues = JSON.DEFAULT_PARSER_FEATURE;
    private Feature[] features;

    private static final Feature[] EMPTY_SERIALIZER_FEATURES = new Feature[0];

    private String jsonData = "{\"result\":[{\"id\":0,\"startDate\":1420041600000,\"name\":\"集团\",\"abbr\":\"集团\",\"endDate\":253402185600000,\"type\":\"1317967b-4a83-442c-a7b4-1ac9e7bf84d9\"},{\"id\":0,\"startDate\":1420041600000,\"name\":\"集团总裁办\",\"abbr\":\"集团总裁办\",\"endDate\":253402185600000,\"pcode\":\"4aa2817e-ae16-4355-a1cc-a73d0b8abc43\",\"type\":\"36e9bde9-2e94-4b91-8b9f-b1078296e3ad\"}],\"errCode\":0,\"success\":true}";

    private Type mType1;//MyResponse
    private Type mType;//MyResponse<List<Dept>>

    ParserConfig config = ParserConfig.getGlobalInstance();
    ParserConfig configBug569 = new ParserConfigBug569();//这个是包含bug的代码

    @Before
    public void init() {
        mType = new TypeReference<MyResponse<List<Dept>>>() {
        }.getType();
        mType1 = new TypeReference<MyResponse>() {
        }.getType();
    }

    //复现
    @Test
    public void testBug569() {
        //第一次反序列化是使用的 MyResponse， 没有指定泛型类型，貌似会缓存 MyResponse， 后面在调用的MyResponse<?>反序列化就受影响了
        MyResponse resp1 = JSON.parseObject(jsonData, mType1, configBug569, featureValues,
                features != null ? features : EMPTY_SERIALIZER_FEATURES);

        //expect MyResponse<JSONArray<JSONObject>>
        MyResponse resp = JSON.parseObject(jsonData, mType, configBug569, featureValues,
                features != null ? features : EMPTY_SERIALIZER_FEATURES);
        Assert.assertNotNull(resp);
        Assert.assertNotNull(resp.getResult());
        Assert.assertEquals(JSONArray.class, resp.getResult().getClass());//这里会受到 resp1 的影响
    }

    //修复
    @Test
    public void testFixBug569() {
        MyResponse resp1 = JSON.parseObject(jsonData, mType1, config, featureValues,
                features != null ? features : EMPTY_SERIALIZER_FEATURES);

        //expect MyResponse<List<Dept>>
        MyResponse resp = JSON.parseObject(jsonData, mType, config, featureValues,
                features != null ? features : EMPTY_SERIALIZER_FEATURES);
        Assert.assertNotNull(resp);
        Assert.assertNotNull(resp.getResult());
        Assert.assertEquals(ArrayList.class, resp.getResult().getClass());
    }

}
