package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class Bug_for_NonStringKeyMap extends TestCase {
    protected void setUp() throws Exception {
        ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.Bug_for_NonStringKeyMap.");
    }

    public void test_bug() throws Exception {
        VO vo = new VO();
        vo.getMap().put(1L, new VAL());
        
        String text = JSON.toJSONString(vo, SerializerFeature.WriteClassName);
        System.out.println(text);
        
        JSON.parse(text);
    }
    
    public void test_1() throws Exception {
        Map<Map<String, String>, String> map = new HashMap<Map<String, String>, String>();
        Map<String, String> submap = new HashMap<String, String>();
        submap.put("subkey", "subvalue");
        map.put(submap, "value");
        String jsonString = JSON.toJSONString(map, SerializerFeature.WriteClassName);
        System.out.println(jsonString);
        Object object = JSON.parse(jsonString);
        System.out.println(object.toString());
    }

    public static class VO {

        private Map<Long, VAL> map = new HashMap<Long, VAL>();

        public Map<Long, VAL> getMap() {
            return map;
        }

        public void setMap(Map<Long, VAL> map) {
            this.map = map;
        }

    }

    public static class VAL {

    }
}
