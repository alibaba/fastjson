package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_10 extends TestCase {

    public void test_0() throws Exception {
        String text = "{'jdbcUrl':\"jdbc:wrap-jdbc:filters=default:name=com.alibaba.dragoon.monitor:jdbc:mysql:\\/\\/10.20.129.167\\/dragoon_v25monitordb?useUnicode=true&characterEncoding=UTF-8\"}";

        JSON.parse(text);
    }

    public void test_1() throws Exception {
        String text = "{'jdbcUrl':'jdbc:wrap-jdbc:filters=default:name=com.alibaba.dragoon.monitor:jdbc:mysql:\\/\\/10.20.129.167\\/dragoon_v25monitordb?useUnicode=true&characterEncoding=UTF-8'}";

        JSON.parse(text);
    }

}
