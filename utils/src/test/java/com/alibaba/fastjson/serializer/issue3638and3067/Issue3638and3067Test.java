package com.alibaba.fastjson.serializer.issue3638and3067;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

/**
 * @author Shilong Li (Lori)
 * @project fastjson
 * @filename Issue3638and3067Test
 * @date 2021/4/17 14:47
 */
public class Issue3638and3067Test {
    JSONObject jo;

    @Before
    public void init() {
        jo = new JSONObject();
    }

    @Test
    public void testTime1() {
        jo.put("d1", "2021-04-17 15:14:00");
        Assert.assertEquals(new Timestamp(jo.getDate("d1").getTime()), jo.getTimestamp("d1"));
    }

    @Test
    public void testTime2() {
        jo.put("d2", "1970-01-01 08:00:00");
        Assert.assertEquals(new Timestamp(jo.getDate("d2").getTime()), jo.getTimestamp("d2"));
    }

    @Test
    public void testTime3() {
        jo.put("d3", "1970-01-01 07:00:00");
        Assert.assertEquals(new Timestamp(jo.getDate("d3").getTime()), jo.getTimestamp("d3"));
    }

    @Test
    public void testTime4() {
        jo.put("d4", "1900-01-01");
        Assert.assertEquals(new Timestamp(jo.getDate("d4").getTime()), jo.getTimestamp("d4"));
    }
}
