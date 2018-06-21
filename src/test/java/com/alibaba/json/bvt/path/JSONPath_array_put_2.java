package com.alibaba.json.bvt.path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_array_put_2 extends TestCase {

    public void test_put() throws Exception {
        Map<String, Object> root = new HashMap<String, Object>();
        List list = new ArrayList();
        root.put("values", list);

        JSONPath.arrayAdd(root, "$.values", 1, 2,3 );

        Assert.assertEquals(3, list.size());
        Assert.assertEquals(1, ((Integer) list.get(0)).intValue());
        Assert.assertEquals(2, ((Integer) list.get(1)).intValue());
        Assert.assertEquals(3, ((Integer) list.get(2)).intValue());
    }

}
