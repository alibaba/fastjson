package com.alibaba.json.bvt.issue_2300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

public class Issue2334 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{\n" +
                "\"EXTINFO\":{\n" +
                "\"bct_loan_account_status[15]\":\"aaa\",\n" +
                "\"wc_bank_num_of_trans_last_3_mon[6]\":-9999,\n" +
                "\"fahai_shixin_post_time[46]\":\"bbb\",\n" +
                "\"zs_punishbreak_regdateclean[22]\":\"ccc\"\n" +
                "}\n" +
                "}";

        JSONObject object = JSON.parseObject(json);

        assertEquals("aaa"
                , JSONPath.eval(object, "$.EXTINFO.bct_loan_account_status\\[15\\]"));

        Object result = JSONPath.extract(json, "$.EXTINFO.bct_loan_account_status\\[15\\]");
        assertEquals("aaa", result.toString());
    }
}
