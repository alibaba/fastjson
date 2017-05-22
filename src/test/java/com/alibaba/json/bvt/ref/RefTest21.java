package com.alibaba.json.bvt.ref;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 16/8/23.
 */
public class RefTest21 extends TestCase {
    public void test_ref() throws Exception {
        String jsonTest = "{\"properties\":{\"type\":{\"items\":{\"allOf\":[{\"$ref\":\"title\",\"required\":[\"iconImg\"]}]}}}}";
        JSONObject object = JSON.parseObject(jsonTest, Feature.DisableSpecialKeyDetect);
        System.out.println( object.get( "properties"));
    }
}
