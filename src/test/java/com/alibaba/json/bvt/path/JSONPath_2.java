package com.alibaba.json.bvt.path;

import org.junit.Assert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_2 extends TestCase {
    public void test_path() throws Exception {
        String json ="{\"user\":[{\"amount\":1.11,\"isadmin\":true,\"age\":18},{\"amount\":0.22,\"isadmin\":false,\"age\":28}]}";
        
        
        {
            JSONArray array = (JSONArray) JSONPath.read(json, "$.user");
            Assert.assertEquals(2, array.size());
            
            Assert.assertTrue(1.11D == array.getJSONObject(0).getDoubleValue("amount"));
            Assert.assertTrue(array.getJSONObject(0).getBoolean("isadmin"));
            Assert.assertTrue(18 == array.getJSONObject(0).getIntValue("age"));
            
            Assert.assertTrue(0.22D == array.getJSONObject(1).getDoubleValue("amount"));
            Assert.assertFalse(array.getJSONObject(1).getBoolean("isadmin"));
            Assert.assertTrue(28 == array.getJSONObject(1).getIntValue("age"));
        }
        
        {
            JSONArray array = (JSONArray) JSONPath.read(json, "$.user[age = 18]");
            Assert.assertEquals(1, array.size());
            
            Assert.assertTrue(1.11D == array.getJSONObject(0).getDoubleValue("amount"));
            Assert.assertTrue(array.getJSONObject(0).getBoolean("isadmin"));
            Assert.assertTrue(18 == array.getJSONObject(0).getIntValue("age"));
        }
        
        {
            JSONArray array = (JSONArray) JSONPath.read(json, "$.user[isadmin = true]");
            Assert.assertEquals(1, array.size());
            
            Assert.assertTrue(1.11D == array.getJSONObject(0).getDoubleValue("amount"));
            Assert.assertTrue(array.getJSONObject(0).getBoolean("isadmin"));
            Assert.assertTrue(18 == array.getJSONObject(0).getIntValue("age"));
        }
        
        {
            JSONArray array = (JSONArray) JSONPath.read(json, "$.user[isadmin = false]");
            Assert.assertEquals(1, array.size());
            
            Assert.assertTrue(0.22D == array.getJSONObject(0).getDoubleValue("amount"));
            Assert.assertFalse(array.getJSONObject(0).getBoolean("isadmin"));
            Assert.assertTrue(28 == array.getJSONObject(0).getIntValue("age"));
        }
        
        {
            JSONArray array = (JSONArray) JSONPath.read(json, "$.user[amount = 0.22]");
            Assert.assertEquals(1, array.size());
            
            Assert.assertTrue(0.22D == array.getJSONObject(0).getDoubleValue("amount"));
            Assert.assertFalse(array.getJSONObject(0).getBoolean("isadmin"));
            Assert.assertTrue(28 == array.getJSONObject(0).getIntValue("age"));
        }
    }
}
