package com.alibaba.json.bvt.bug;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_xuzebin extends TestCase {

    public void testMap() {
        P p = new P();
        p.setI(2);
        p.getMap().put("a", "b");
        String json = JSON.toJSONString(p, SerializerFeature.WriteClassName);
        System.out.println(json);

        P x = JSON.parseObject(json, P.class);
        System.out.println(JSON.toJSONString(x));
    }

    public void testMap2() {
        P p = new P();
        p.setI(2);
        // p.getMap().put("a", "b");
        String json = JSON.toJSONString(p, SerializerFeature.WriteClassName);
        System.out.println(json);

        P x = JSON.parseObject(json, P.class);
        System.out.println(JSON.toJSONString(x));
    }

    public static class P {

        private Map<String, String> map = new ConcurrentHashMap<String, String>();
        private int                 i   = 0;

        public Map<String, String> getMap() {
            return map;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

    }
}
