package com.alibaba.json.bvt;

import java.util.Collections;

import org.junit.Assert;

import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class JSONObjectFluentTest extends TestCase {

    public void test_fluent() throws Exception {
        JSONObject object = new JSONObject() //
                                             .fluentPut("1", 1001) //
                                             .fluentPut("2", 1002);

        Assert.assertEquals(2, object.size());

        object.fluentPutAll(Collections.singletonMap("3", 1003)) //
              .fluentPutAll(Collections.singletonMap("4", 1004));

        Assert.assertEquals(4, object.size());

        object.fluentRemove("1") //
              .fluentRemove("2");
        
        Assert.assertEquals(2, object.size());
        
        object.fluentClear().fluentClear();
        
        Assert.assertEquals(0, object.size());
    }

}
