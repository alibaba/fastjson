package com.alibaba.json.bvt;

import java.util.Currency;
import java.util.Locale;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class CurrencyTest extends TestCase {

    public void test_0() throws Exception {
        VO vo = new VO();
        vo.setValue(Currency.getInstance(Locale.CHINA));
        String text = JSON.toJSONString(vo);
        VO vo2 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo2.value, vo.value);
    }

    public void test_null() throws Exception {
        VO vo = new VO();
        vo.setValue(null);
        String text = JSON.toJSONString(vo);
        VO vo2 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo2.value, vo.value);
    }

    public static class VO {

        private Currency value;

        public Currency getValue() {
            return value;
        }

        public void setValue(Currency value) {
            this.value = value;
        }

    }
}
