package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.Date;

/**
 * Created by wenshao on 10/01/2017.
 */
public class Issue978 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.date = new Date(1483413683714L);

        JSONObject obj = (JSONObject) JSON.toJSON(model);
        assertEquals("{\"date\":\"2017-01-03 11:21:23\"}", obj.toJSONString());
    }

    public static class Model {
        @JSONField(format="yyyy-MM-dd HH:mm:ss")
        public Date date;
    }
}
