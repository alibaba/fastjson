package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 15/02/2017.
 */
public class Issue_for_zuojing extends TestCase {
    public void test_for_zuojing() throws Exception {
        String rowData = "[{\"@type\":\"java.util.HashMap\",\"end_date\":{\"@type\":\"java.sql.Date\",\"val\":1490803200000},\"gmt_create\":{\"@type\":\"java.sql.Timestamp\",\"val\":1487139144000},\"arr_city\":\"FOC\",\"agent\n" +
                "_id\":4765L,\"auto_book\":0B,\"sale_rebase\":12,\"channel\":1B,\"dep_city\":\"BJS\",\"gmt_modified\":{\"@type\":\"java.sql.Timestamp\",\"val\":1487139144000},\"is_support_share\":1B,\"sale_retenti\n" +
                "on\":430S,\"invoice_type\":5B,\"id\":12675100456,\"start_date\":{\"@type\":\"java.sql.Date\",\"val\":1485878400000},\"pat\":1B,\"agent_sub_nick\":\"辰\",\"travel_start_date\":{\"@type\"\n" +
                ":\"java.sql.Date\",\"val\":1485878400000},\"policy_type\":2B,\"travel_end_date\":{\"@type\":\"java.sql.Date\",\"val\":1490803200000},\"flights_limit_type\":1B,\"officeid\":\"WNZ159\",\"future_tic\n" +
                "ket\":0B,\"fare_id\":80L,\"source_id\":4653492L,\"source_code\":32B,\"agent_sub_id\":2752916259,\"flights_limit\":\"1100-1999,4000-4999,8200-8230,8960\"},{\"$ref\":\"$[0]\"}]";
        List row = JSON.parseObject(rowData,List.class);
        assertEquals(2, row.size());
        assertSame(row.get(0), row.get(1));
    }
}
