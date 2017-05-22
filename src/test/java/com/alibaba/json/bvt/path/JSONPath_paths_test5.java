package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.Map;

/**
 * Created by wuwen on 2016/12/27.
 */
public class JSONPath_paths_test5 extends TestCase {

    public void test_array() throws Exception {
        String[] array = new String[]{"1001", "wenshao"};

        Map<String, Object> paths = JSONPath.paths(array);

        Assert.assertEquals(3, paths.size());
        Assert.assertSame(array, paths.get("/"));
        Assert.assertEquals("1001", paths.get("/0"));
        Assert.assertEquals("wenshao", paths.get("/1"));
    }
}
