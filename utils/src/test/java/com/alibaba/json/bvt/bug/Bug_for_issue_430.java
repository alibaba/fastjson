package com.alibaba.json.bvt.bug;

import java.util.ArrayList;

import com.alibaba.fastjson.parser.ParserConfig;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import junit.framework.TestCase;

public class Bug_for_issue_430 extends TestCase {
    protected void setUp() throws Exception {
        ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.Bug_for_issue_430");
    }

    public void test_for_issue() throws Exception {
        String text = "[{\"@type\": \"com.alibaba.json.bvt.bug.Bug_for_issue_430$FooModel\", \"fooCollection\": null}, {\"@type\": \"com.alibaba.json.bvt.bug.Bug_for_issue_430$FooModel\", \"fooCollection\": null}]";
        JSONArray array = JSON.parseArray(text);
        Assert.assertEquals(FooModel.class, array.get(0).getClass());
        Assert.assertEquals(FooModel.class, array.get(1).getClass());
        
        Assert.assertNull(((FooModel)array.get(0)).fooCollection);
        Assert.assertNull(((FooModel)array.get(1)).fooCollection);
    }
    
    public void test_for_issue_1() throws Exception {
        String text = "[{\"@type\": \"com.alibaba.json.bvt.bug.Bug_for_issue_430$FooModel\", \"fooCollection\": null}, {\"@type\": \"com.alibaba.json.bvt.bug.Bug_for_issue_430$FooModel\", \"fooCollection\": null}]";
        JSONArray array = (JSONArray) JSON.parse(text);
        Assert.assertEquals(FooModel.class, array.get(0).getClass());
        Assert.assertEquals(FooModel.class, array.get(1).getClass());
        
        Assert.assertNull(((FooModel)array.get(0)).fooCollection);
        Assert.assertNull(((FooModel)array.get(1)).fooCollection);
    }

    public static class FooModel {

        public ArrayList<FooCollectionModel> fooCollection;
    }

    public static class FooCollectionModel {

    }
}
