package com.alibaba.json.test.bvt.serializer.indent;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class PrettyFormatTest extends TestCase {

    public void test_0() throws Exception {
        Assert.assertEquals(0, new JSONSerializer().getIndentCount());
        
        Assert.assertEquals("[\n\t{},\n\t{}\n]", JSON.toJSONString(new Object[] { new Object(), new Object() }, SerializerFeature.PrettyFormat));
    }
}
