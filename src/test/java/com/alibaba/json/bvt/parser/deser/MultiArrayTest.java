package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class MultiArrayTest extends TestCase {

    public void test_0() throws Exception {
        String[][] array = new String[][] { new String[] { "a", "b" }, new String[] { "c", "d", "e" } };
        String text = JSON.toJSONString(array);
        String[][] array2 = JSON.parseObject(text, String[][].class);
        Assert.assertEquals("a", array2[0][0]);
        Assert.assertEquals("b", array2[0][1]);
        Assert.assertEquals("c", array2[1][0]);
        Assert.assertEquals("d", array2[1][1]);
        Assert.assertEquals("e", array2[1][2]);
    }
}
