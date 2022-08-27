package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;
import org.junit.Assert;

public class JSONPath_issue1208 extends TestCase {

    public void test_largeNumberProperty() throws Exception {
        String json1 = "{\"articles\":{\"2147483647\":{\"XXX\":\"xiu\"}}}";
        String path1 = "$.articles.2147483647.XXX";
        Object read = JSONPath.read(json1, path1);
        Assert.assertEquals("xiu", read);

        String json2 = "{\"articles\":{\"2147483648\":{\"XXX\":\"xiu\"}}}";
        String path2 = "$.articles.2147483648.XXX";
        Object read2 = JSONPath.read(json2, path2);

        Assert.assertEquals("xiu", read2);
    }


}
