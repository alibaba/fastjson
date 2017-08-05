package com.alibaba.json.bvt.issue_1300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by wenshao on 05/08/2017.
 */
public class Issue1371 extends TestCase {
    private enum Rooms{
        A, B, C, D ,E ;
    }

    public void testFastjsonEnum(){

        Map<Rooms, Rooms> enumMap = new TreeMap<Rooms, Rooms>();

        enumMap.put(Rooms.C, Rooms.D);
        enumMap.put(Rooms.E, Rooms.A);

        Assert.assertEquals(JSON.toJSONString(enumMap, SerializerFeature.WriteNonStringKeyAsString),
                "{\"C\":\"D\",\"E\":\"A\"}");

    }




//    public void testParsed(){
//
//        String oldStyleJson = "{1:'abc', 2:'cde'}";
//
//        Gson gson = new Gson();
//
//        Map fromJson = gson.fromJson(oldStyleJson, Map.class);
//
//        Assert.assertNull(fromJson.get(1));
//
//        Assert.assertEquals(fromJson.get("1"), "abc" );
//
//        Map parsed = JSON.parseObject(oldStyleJson, Map.class, Feature.IgnoreAutoType, Feature.DisableFieldSmartMatch);
//
//
//        Assert.assertNull(parsed.get(1));
//
//        Assert.assertEquals(parsed.get("1"), "abc" );
//
//    }
//
//    public void testParsed_jackson() throws Exception {
//
//        String oldStyleJson = "{1:\"abc\", 2:\"cde\"}";
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map fromJson = objectMapper.readValue(oldStyleJson, Map.class);
//        Assert.assertNull(fromJson.get(1));
//    }
}
