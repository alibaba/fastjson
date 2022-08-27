package com.alibaba.json.bvt.serializer;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class NoneStringKeyTest extends TestCase {

    public void test_0() throws Exception {
        Map map = new HashMap();
        map.put(1, 101);

        Assert.assertEquals("{1:101}", JSON.toJSONString(map));
    }

    public void test_1() throws Exception {
        Map map = new HashMap();
        map.put(1, 101);

        Assert.assertEquals("{\"1\":101}", JSON.toJSONString(map, SerializerFeature.BrowserCompatible));
    }

    public void test_2() throws Exception {
        Map map = new HashMap();
        map.put(1, 101);

        Assert.assertEquals("{\"1\":101}", JSON.toJSONString(map, SerializerFeature.WriteNonStringKeyAsString));
    }

    public void test_null_0() throws Exception {
        Map map = new HashMap();
        map.put(null, 101);

        Assert.assertEquals("{null:101}", JSON.toJSONString(map));
    }

    public void test_3() throws Exception {
        Map map = new HashMap();
        map.put(null, 101);

        Assert.assertEquals("{\"null\":101}", JSON.toJSONString(map, SerializerFeature.WriteNonStringKeyAsString));
    }

    public void test_4() throws Exception {
        SubjectDTO dto = new SubjectDTO();
        dto.getResults().put(3, new Result());

        String json = JSON.toJSONString(dto);
        assertEquals("{\"results\":{3:{}}}", json);

        SubjectDTO dto2 = JSON.parseObject(json, SubjectDTO.class, Feature.NonStringKeyAsString);
        System.out.println(JSON.toJSONString(dto2.getResults()));
    }

    public static class Result {

    }

    public static class SubjectDTO {
        private Map<Integer, Result> results = new HashMap<Integer, Result>();

        public Map<Integer, Result> getResults() {
            return results;
        }
    }
}
