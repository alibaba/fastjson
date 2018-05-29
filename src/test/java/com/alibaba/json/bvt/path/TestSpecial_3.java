package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class TestSpecial_3 extends TestCase {

    public void test_special() throws Exception {
        String json = "[{\"@type\":\"NAME_CORRECTION\",\"value\":23}]";
        JSONArray array = (JSONArray) JSON.parse(json, Feature.DisableSpecialKeyDetect);
        Object obj = JSONPath.eval(array, "[\\@type='NAME_CORRECTION']");
        assertNotNull(obj);
    }

    public void test_special_1() throws Exception {
        String json = "[{\":lang\":\"NAME_CORRECTION\",\"value\":23}]";
        JSONArray array = (JSONArray) JSON.parse(json, Feature.DisableSpecialKeyDetect);
        Object obj = JSONPath.eval(array, "[\\:lang='NAME_CORRECTION']");
        assertNotNull(obj);
    }

    public void test_special_2() throws Exception {
        String json = "{\"cpe-item\":{\"@name\":\"cpe:/a:google:chrome:4.0.249.19\",\"cpe-23:cpe23-item\":{\"@name\":\"cpe:2.3:a:google:chrome:4.0.249.19:*:*:*:*:*:*:*\"},\"title\":[{\"#text\":\"グーグル クローム 4.0.249.19\",\"@xml:lang\":\"ja-JP\"},{\"#text\":\"Google Chrome 4.0.249.19\",\"@xml:lang\":\"en-US\"}]}}";
        String path = "['cpe-item']['title'][\\@xml\\:lang='en-US']['#text'][0]";
        JSONObject object = (JSONObject) JSON.parse(json, Feature.DisableSpecialKeyDetect);
        Object obj = JSONPath.eval(object, path);
        assertNotNull(obj);
    }
}
