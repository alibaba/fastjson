package com.alibaba.json.bvt.issue_1100;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 24/04/2017.
 */
public class Issue1150 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = JSON.parseObject("{\"values\":\"\"}", Model.class);
        assertNull(model.values);
    }

    public void test_for_issue_array() throws Exception {
        Model2 model = JSON.parseObject("{\"values\":\"\"}", Model2.class);
        assertNull(model.values);
    }

    public static class Model {
        public List values;
    }

    public static class Model2 {
        public Item[] values;
    }

    public static class Item {

    }
}
