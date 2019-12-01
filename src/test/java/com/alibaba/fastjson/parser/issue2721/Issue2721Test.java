package com.alibaba.fastjson.parser.issue2721;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONPath;
import org.junit.Assert;
import org.junit.Test;

public class Issue2721Test {
    @Test
    public void test2721() {
        String chineseKeyString = "[{\"名称\": \"脆皮青豆\", \"配料\": [\"豌豆\", \"棕榈油\", \"白砂糖\", \"食用盐\", \"玉米淀粉\"]}]";
        String normalKeyString = "[{ \"name\": \"脆皮青豆\", \"配料\": [\"豌豆\", \"棕榈油\", \"白砂糖\", \"食用盐\", \"玉米淀粉\"] }]";

        System.out.println(JSONPath.read(chineseKeyString, "$[名称 = '脆皮青豆']"));
        // [{"名称":"脆皮青豆","配料":["豌豆","棕榈油","白砂糖","食用盐","玉米淀粉"]}]
        System.out.println(JSONPath.read(normalKeyString, "$[name = '脆皮青豆']"));
        // [{"name":"脆皮青豆","配料":["豌豆","棕榈油","白砂糖","食用盐","玉米淀粉"]}]

        Assert.assertFalse("Chinese Key is NOT OK, Array length is 0!", ((JSONArray) JSONPath.read(chineseKeyString, "$[名称 = '脆皮青豆']")).isEmpty());
        Assert.assertFalse("Chinese Key is NOT OK, Array length is 0!", ((JSONArray) JSONPath.read(normalKeyString, "$[name = '脆皮青豆']")).isEmpty());
    }
}
