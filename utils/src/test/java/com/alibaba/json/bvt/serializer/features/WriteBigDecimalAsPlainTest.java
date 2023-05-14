package com.alibaba.json.bvt.serializer.features;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;
import org.junit.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by wenshao on 16/8/9.
 */
public class WriteBigDecimalAsPlainTest extends TestCase {
    public void test_for_feature() throws Exception {
        BigDecimal value = new BigDecimal("0.00000001");

        Assert.assertEquals("1E-8", JSON.toJSONString(value));
        Assert.assertEquals("0.00000001", JSON.toJSONString(value, SerializerFeature.WriteBigDecimalAsPlain));
    }

    public void test_1() throws Exception {
        Model m = new Model();
        m.value = new BigDecimal("0.00000001");

        Assert.assertEquals("{\"value\":1E-8}", JSON.toJSONString(m));
        Assert.assertEquals("{\"value\":0.00000001}", JSON.toJSONString(m, SerializerFeature.WriteBigDecimalAsPlain));
    }

    public void test_for_feature_BigInteger() throws Exception {
        BigInteger value = new BigInteger("2020020700826004000000000000");

        Assert.assertEquals("2020020700826004000000000000", JSON.toJSONString(value));
        Assert.assertEquals("2020020700826004000000000000", JSON.toJSONString(value, SerializerFeature.WriteBigDecimalAsPlain));
    }

    public static class Model {
        private BigDecimal value;

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }
    }

    public static class ModelBigInteger {
        private BigInteger value;

        public BigInteger getValue() {
            return value;
        }

        public void setValue(BigInteger value) {
            this.value = value;
        }
    }
}
