package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.json.bvtVO.Issue900_CurrencyListResult;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by wenshao on 2016/11/18.
 */
public class Issue_900_4 extends TestCase {
    public void test_for_issue() throws Exception {
        Issue900_CurrencyListResult result = JSON.parseObject("{\"payCurrencyList\":[\"abc\"]}", Issue900_CurrencyListResult.class, Feature.SupportNonPublicField);
        assertEquals(1, result.getPayCurrencyList().size());
        assertEquals("abc", result.getPayCurrencyList().get(0));
    }

    public static class CurrencyListResult {
        public static final int    VERSION = 1; // cache version, 如果结构改变, 需要同步修改版本号

        // 可支付币种列表
        private ArrayList<String> payCurrencyList;

        public ArrayList<String> getPayCurrencyList() {
            return payCurrencyList;
        }

    }
}
