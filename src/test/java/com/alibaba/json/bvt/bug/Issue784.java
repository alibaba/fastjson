package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 16/8/24.
 */
public class Issue784 extends TestCase {
    public void test_for_issue() throws Exception {
        JSON.parse("[{\"args\":[\"150\",\"change\",{\"timeStamp\":1471595047319,\"value\":\"\"},{\"attrs\":{\"value\":\"\"}}],\"method\":\"fireEvent\"}]");
    }
}
