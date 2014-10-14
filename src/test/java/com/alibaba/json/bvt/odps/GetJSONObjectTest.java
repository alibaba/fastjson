package com.alibaba.json.bvt.odps;

import junit.framework.TestCase;

import com.alibaba.fastjson.support.odps.udf.JSONExtract;


public class GetJSONObjectTest extends TestCase {
    public void test_udf() throws Exception {
        JSONExtract udf = new JSONExtract();
        String result = udf.evaluate("{\"id\":123,\"name\":\"wenshao\"}", "$");
        System.out.println(result);
    }
}
