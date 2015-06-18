package com.alibaba.json.bvt.serializer;

import java.nio.charset.Charset;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class CharsetTest extends TestCase {

    public void test_file() throws Exception {
        Charset c = Charset.defaultCharset();

        String text = JSON.toJSONString(c);

        Assert.assertEquals(JSON.toJSONString(c.toString()), text);

        Charset c1 = JSON.parseObject(text, Charset.class);
        Assert.assertEquals(c.toString(), c1.toString());
    }
}
