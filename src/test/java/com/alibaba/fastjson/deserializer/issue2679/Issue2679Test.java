package com.alibaba.fastjson.deserializer.issue2679;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Issue2679Test {

    static class Foo {
        private Map<String, String> mapData;

        public Map<String, String> getMapData() {
            return mapData;
        }

        public void setMapData(Map<String, String> mapData) {
            this.mapData = mapData;
        }
    }

    @Test
    public void issue2679() {
        List<Foo> fooList = new ArrayList<Foo>();
        HashMap<String, String> mapData = new HashMap<String, String>();
        mapData.put("key1", "val1");

        Foo foo = new Foo();
        foo.setMapData(mapData);

        Foo foo2 = new Foo();
        foo2.setMapData(mapData);

        fooList.add(foo);
        fooList.add(foo2);

        String fooListJson = JSON.toJSONString(fooList);

        List<Foo> fooListJsonParseRet = JSON.parseArray(fooListJson, Foo.class);
        Assert.assertTrue(fooListJsonParseRet.get(0).getMapData() == fooListJsonParseRet.get(1).getMapData());

        String json = "[{\"mapData\":{\"key1\":\"val1\"}},{\"mapData\":{\"$ref\":\"ref\"}}]";
        fooListJsonParseRet = JSON.parseArray(json, Foo.class);
        Assert.assertEquals("ref", fooListJsonParseRet.get(1).getMapData().get("$ref"));
    }
}
