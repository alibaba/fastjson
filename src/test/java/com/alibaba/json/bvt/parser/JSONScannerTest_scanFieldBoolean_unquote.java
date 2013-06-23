package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvt.parser.JSONScannerTest_scanFieldBoolean.VO;

public class JSONScannerTest_scanFieldBoolean_unquote extends TestCase {

    public void test_4() throws Exception {
        String text = "{\"value\":false,id:2}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertEquals(false, obj.getValue());
    }
}
