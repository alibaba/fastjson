package com.alibaba.json.bvt.issue_2700;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class Issue2721Test extends TestCase
{
    public void test2721() {
        String chineseKeyString = "[{\"名称\": \"脆皮青豆\", \"配料\": [\"豌豆\", \"棕榈油\", \"白砂糖\", \"食用盐\", \"玉米淀粉\"]}]";
        System.out.println(JSONPath.read(chineseKeyString, "$[名称 = '脆皮青豆']"));
        // [{"名称":"脆皮青豆","配料":["豌豆","棕榈油","白砂糖","食用盐","玉米淀粉"]}]

        String normalKeyString = "[{ \"name\": \"脆皮青豆\", \"配料\": [\"豌豆\", \"棕榈油\", \"白砂糖\", \"食用盐\", \"玉米淀粉\"] }]";
        System.out.println(JSONPath.read(normalKeyString, "$[name = '脆皮青豆']"));
        // [{"name":"脆皮青豆","配料":["豌豆","棕榈油","白砂糖","食用盐","玉米淀粉"]}]
//
        Assert.assertFalse("Chinese Key is NOT OK, Array length is 0!", ((List) JSONPath.read(chineseKeyString, "$[名称 = '脆皮青豆']")).isEmpty());
        Assert.assertFalse("Chinese Key is NOT OK, Array length is 0!", ((List) JSONPath.read(normalKeyString, "$[name = '脆皮青豆']")).isEmpty());
    }
}