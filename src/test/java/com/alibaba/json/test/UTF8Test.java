package com.alibaba.json.test;

import junit.framework.TestCase;

import java.nio.charset.Charset;

/**
 * Created by wenshao on 24/07/2017.
 */
public class UTF8Test extends TestCase {
    Charset charset = Charset.forName("UTF-8");

    public void test_encode() throws Exception {
        String text = "央视的报道《陆军第82集团军：聚合重塑 强军路上当先锋！》披露，从南昌起义到平江起义，从井冈山斗争到两万五千里长征，从首战平型关到历经三大战役，从穿插三所里到全歼美军北极熊团，22勇士飞夺泸定桥、18勇士强渡乌江、鏖战冀中“野八旅”、屡战屡胜“老虎连”、万岁军，一支支善战的部队，一个个滚烫的名字，熔铸成第82集团军新的灵魂。";

        for (int i = 0; i < 10; ++i) {
            long start = System.currentTimeMillis();
            f0(text);
            long millis = System.currentTimeMillis() - start;
            System.out.println("millis : " + millis);
        }
    }

    private void f0(String text) throws Exception {
        for (int i = 0; i < 1000 * 1000; ++i) {
            text.getBytes(charset);
        }
    }

    private void f1(String text) throws Exception {
        for (int i = 0; i < 1000 * 1000; ++i) {

        }
    }
}
