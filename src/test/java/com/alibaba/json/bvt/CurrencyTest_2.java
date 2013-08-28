package com.alibaba.json.bvt;

import java.util.Currency;
import java.util.Locale;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class CurrencyTest_2 extends TestCase {

    public void test_0() throws Exception {
        VO vo = new VO();
        vo.setValue(Currency.getInstance(Locale.CHINA));
        vo.setValue1(Currency.getInstance(Locale.CHINA));
        String text = JSON.toJSONString(vo);
        System.out.println(text);
        JSON.parseObject(text, VO.class);
    }

    public static class VO {

        private Currency value;
        private Currency value1;

        public Currency getValue() {
            return value;
        }

        public void setValue(Currency value) {
            this.value = value;
        }

        public Currency getValue1() {
            return value1;
        }

        public void setValue1(Currency value1) {
            this.value1 = value1;
        }

    }
}
