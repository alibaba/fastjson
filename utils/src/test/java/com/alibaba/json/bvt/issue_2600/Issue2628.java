package com.alibaba.json.bvt.issue_2600;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.math.BigInteger;

public class Issue2628 extends TestCase {
    public void test_for_issue() throws Exception {
        long MAX_LONG = Long.MAX_VALUE; //9223372036854775807
        long MIN_LONG = Long.MIN_VALUE; //-9223372036854775808

        String s1 = "9423372036854775807"; //-9423372036854775808
        BigInteger bi1 = JSON.parseObject(s1, BigInteger.class); //没问题
        assertEquals("9423372036854775807", bi1.toString());

        BigInteger bi2 = new BigInteger(s1); //没问题
        assertEquals("9423372036854775807", bi2.toString());

        Tobject tobj1 = new Tobject();
        tobj1.setBi(bi2); //没问题
        assertEquals("9423372036854775807", tobj1.getBi().toString());;

        String s2 = JSON.toJSONString(tobj1);
        Tobject tobj2 = JSON.parseObject(s2, Tobject.class);  //有问题
        assertEquals("9423372036854775807", tobj2.getBi().toString());
    }

    static class Tobject {
        private BigInteger bi;

        public BigInteger getBi() {
            return bi;
        }
        public void setBi(BigInteger bi) {
            this.bi = bi;
        }
    }
}
