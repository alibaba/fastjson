package com.alibaba;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class main {
    public static  void main(String[] args) {
        //System.out.println(1);
        Map<Object, Object> map = new HashMap();
        map.put(1, "1");
        map.put(2, 2);
        map.put(Boolean.valueOf("false"), "fa");
        map.put("false", "lse");
        String s = JSONObject.toJSONString(map);
        System.out.println(s);
        JSONObject j = JSONObject.parseObject(s);
        j.put("3", null);
        System.out.println(j.keySet());
        System.out.println(j.containsValue(null));
    }

}
