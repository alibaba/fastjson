package com.alibaba.json.bvt.bug.bug2019;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;
import lombok.AllArgsConstructor;
import org.junit.Assert;

import java.math.BigInteger;

public class Bug20190903_for_BigInteger extends TestCase
{
    public void test_for_issue() throws Exception {
        System.out.println(Long.MAX_VALUE);
            String rawStr = "-9223372036854775808";
            StringTest a = new StringTest(rawStr);
            String jsonStr = JSON.toJSONString(a);
            IntegerTest b = JSONObject.parseObject(jsonStr,IntegerTest.class);
            Assert.assertEquals(new BigInteger(a.getValue()), b.getValue());
    }



    public static class IntegerTest {
        BigInteger value;

        public BigInteger getValue() {
            return value;
        }

        public void setValue(BigInteger value) {
            this.value = value;
        }

        public IntegerTest() {
        }
    }



    public class StringTest{
        String value;

        public StringTest(String value) {
            this.value = value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public StringTest() {
        }
    }
}

