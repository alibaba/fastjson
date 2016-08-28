package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 16/8/28.
 */
public class Issue778 extends TestCase {
    public void test_for_issue() throws Exception {
        JSON.toJSON(new RequestData(){});
    }

    public abstract class RequestData{
    }
}
