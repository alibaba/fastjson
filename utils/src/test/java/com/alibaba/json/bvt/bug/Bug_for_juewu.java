package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;


public class Bug_for_juewu extends TestCase {
    public void test_str() throws Exception {
        String text = "{\"weitao_feed\":{\"head\":{\"Version\":\"V1.0\",\"Status\":\"OK\",\"SearchTime\":1488,\"DocsReturn\":18,\"DocsFound\":20,\"DocsRestrict\":20,\"DocsSearch\":0},\"auctions\":[{\"id\":\"110009362197\",\"creator_id\":\"673515636\",\"gmt_create_ms\":\"1385540374000\"}]}}";

    }
}
