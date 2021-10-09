package com.alibaba.json.bvt.issue_3900;

import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

import java.util.List;

/**
 * @author mikawudi
 * @description  https://github.com/alibaba/fastjson/issues/3903
 * @created 2021/10/9
 **/
public class Issue3903 extends TestCase {

    private final static String DATA = "{\"status\":\"success\",\"data\":{\"resultType\":\"matrix\",\"result\":[{\"metric\":{},\"values\":[[1632273205,\"3\"],[1632273210,\"3\"],[1632273215,\"3\"],[1632273220,\"3\"],[1632273225,\"3\"],[1632273230,\"3\"],[1632273235,\"3\"],[1632273240,\"3\"],[1632273245,\"3\"],[1632273250,\"3\"],[1632273255,\"3\"],[1632273260,\"3\"],[1632273265,\"3\"],[1632273270,\"3\"],[1632273275,\"3\"],[1632273280,\"3\"],[1632273285,\"3\"],[1632273290,\"3\"],[1632273295,\"3\"],[1632273300,\"3\"],[1632273305,\"3\"]]}]}}\n";
    public void test_for_issue() throws Exception {
        List result = (List)JSONPath.eval(DATA, "$.data.result[0].values[*][1]");
        assertEquals(result.size(), 21);
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i), "3");
        }
    }
}
