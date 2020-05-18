package com.alibaba.json.bvt.parser.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.List;

/**
 * 这个bug由李先锋反馈
 * @author wenshao
 *
 */
public class Bug_for_guanxiu extends TestCase {

    public void test_long_list() throws Exception {
        String str = "{\"tnt_inst_id\":\"MYBKC1CN\",\"interface_id\":\"ant.mybank.loantrade.existvalidloan.query @1.0.0\",\"template_id\":\"EX-SYNC-IN-\n" +
                "\n" +
                "OPEN-API\"}";

        JSON.parseObject(str);
    }
}
