package com.alibaba.json.bvt.serializer.features;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;
import org.junit.Assert;

import java.math.BigDecimal;

/**
 * Created by wenshao on 16/8/9.
 */
public class WriteBigDecimalAsPlainTest extends TestCase {
    public void test_for_feature() throws Exception {
        BigDecimal value = new BigDecimal("0.00000001");

        Assert.assertEquals("1E-8", JSON.toJSONString(value));
        Assert.assertEquals("0.00000001", JSON.toJSONString(value, SerializerFeature.WriteBigDecimalAsPlain));
    }
}
