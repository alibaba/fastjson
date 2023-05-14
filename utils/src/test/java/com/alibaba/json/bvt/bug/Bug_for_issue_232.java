package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class Bug_for_issue_232 extends TestCase {
    public void test_for_issue() throws Exception {
        String source = "{\"code\": 0, \"data\": {\"country\": \"China\", \"country_id\": \"CN\", \"area\": \"East China\", \"area_id\": \"300000\", \"region\": \"Jiangsu Province \",\" region_id \":\" 320000 \",\" city \":\" Nanjing \",\" city_id \":\" 320100 \",\" county \":\" \",\" county_id \":\" - 1 \",\" isp \":\" China Unicom \",\" isp_id \":\" 100026 \",\" ip \":\" 58.240.65.50 \"}}";
        JSONObject object = JSONObject.parseObject (source);
        Assert.assertEquals(0, object.getIntValue("code"));
    }
}
