package com.alibaba.json.bvt.odps;

import junit.framework.TestCase;

import com.alibaba.fastjson.support.odps.udf.GetJSONObject;


public class GetJSONObjectTest extends TestCase {
    public void test_udf() throws Exception {
        GetJSONObject udf = new GetJSONObject();
        String result = udf.evaluate("{\"id\":123,\"name\":\"wenshao\"}", "$");
        System.out.println(result);
    }
}
