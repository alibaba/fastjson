package com.alibaba.json.bvt.path;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_deepScan_test extends TestCase {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void test_0() throws Exception {
        Map root = Collections.singletonMap("company", //
                                            Collections.singletonMap("departs", //
                                                                     Arrays.asList( //
                                                                                    Collections.singletonMap("id",
                                                                                                             1001), //
                                                                                    Collections.singletonMap("id",
                                                                                                             1002), //
                                                                                    Collections.singletonMap("id", 1003) //
                                                                     ) //
                                            ));

        List<Object> ids = (List<Object>) JSONPath.eval(root, "$..id");
        Assert.assertEquals(3, ids.size());
        Assert.assertEquals(1001, ids.get(0));
        Assert.assertEquals(1002, ids.get(1));
        Assert.assertEquals(1003, ids.get(2));
    }

    public static class Root {

    }
}
