package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 2016/11/18.
 */
public class Issue_900_3 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = JSON.parseObject("{\"items\":[\"abc\"]}", Model.class, Feature.SupportNonPublicField);
        assertEquals(1, model.items.size());
        assertEquals("abc", model.items.get(0));
    }

    public static class Model {
        private List<String> items;
    }
}
