package com.alibaba.json.bvt.serializer;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.DoubleSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;

public class DoubleTest_custom2 extends TestCase {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void test_0() throws Exception {
        Map values = new HashMap();
        Double v = 9.00;
        values.put("double", v);

        SerializeConfig config = new SerializeConfig();
        config.put(Double.class, new DoubleSerializer(new DecimalFormat("###.00")));
        Assert.assertEquals("{\"double\":9.00}", JSON.toJSONString(values, config));
    }

}
