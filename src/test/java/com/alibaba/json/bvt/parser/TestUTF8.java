package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TestUTF8 extends TestCase {

    public void test_utf() throws Exception {
        JSONObject obj = (JSONObject) JSON.parse("{'name':'刘大'}".getBytes("UTF-8"));
        Assert.assertEquals(1, obj.size());
        Assert.assertEquals("刘大", obj.get("name"));
    }

    public void test_utf_cn() throws Exception {
        String content = "首先来到村委会，走进农家书屋，认真翻看各种图书和报刊。他拿起一份藏文版《人民日报》，询问村民读书读报的情况，并和正在读书的几位藏族青年亲切交谈，勉励他们好好学习，学以致用，培养致富本领。在党支部活动室，村支部书记桑杰介绍了支部建设情况。当听到全村36名党员发挥先锋模范作用";
        JSONObject json = new JSONObject();
        json.put("content", content);
        JSONObject obj = (JSONObject) JSON.parse(json.toJSONString().getBytes("UTF-8"));
        Assert.assertEquals(1, obj.size());
        Assert.assertEquals(content, obj.get("content"));
    }

    public void test_utf_de() throws Exception {
        String content = "Beim Griechenland-Gipfel gibt es viele Gewinner. Kanzlerin Merkel bekommt die Bankenbeteiligung, Frankreichs Präsident Sarkozy den Aufkauf von Staatsanleihen. \\nEinzig EZB-Präsident Jean-Claude Trichet gilt als Verlierer. Er zog im Machtkampf den Kürzeren";
        JSONObject json = new JSONObject();
        json.put("content", content);
        JSONObject obj = (JSONObject) JSON.parse(json.toJSONString().getBytes("UTF-8"));
        Assert.assertEquals(1, obj.size());
        Assert.assertEquals(content, obj.get("content"));
    }

    public void test_utf_jp() throws Exception {
        String content = "菅首相がマニフェストで陳謝";
        JSONObject json = new JSONObject();
        json.put("content", content);
        JSONObject obj = (JSONObject) JSON.parse(json.toJSONString().getBytes("UTF-8"));
        Assert.assertEquals(1, obj.size());
        Assert.assertEquals(content, obj.get("content"));
    }

    public void test_utf_() throws Exception {
        String content = "Viel Spaß mit Java 7 und Eclipse!";
        JSONObject json = new JSONObject();
        json.put("content", content);
        JSONObject obj = (JSONObject) JSON.parse(json.toJSONString().getBytes("UTF-8"));
        Assert.assertEquals(1, obj.size());
        Assert.assertEquals(content, obj.get("content"));
    }

    public void test_utf_7() throws Exception {
        String content = "薄扶林水塘，《香港雜記》叫百步林水塘，係香港一水塘，亦係全港第一個水塘。水塘喺香港島西嘅薄扶林，近薄扶林村。佢集有薄扶林谷地中咁多條河涌嘅水。涌水出自扯旗山、西高山、爐峯峽、歌賦山、奇力山。水塘分上下兩塘，儲水量約為廿六萬立方米，約莫六千八百萬加侖。水塘溢水會經薄扶林村流入瀑布灣";
        JSONObject json = new JSONObject();
        json.put("content", content);
        JSONObject obj = (JSONObject) JSON.parse(json.toJSONString().getBytes("UTF-8"));
        Assert.assertEquals(1, obj.size());
        Assert.assertEquals(content, obj.get("content"));
    }

}
