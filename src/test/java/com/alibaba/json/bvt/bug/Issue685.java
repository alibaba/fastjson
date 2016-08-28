package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.Date;

/**
 * Created by wenshao on 16/8/28.
 */
public class Issue685 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.publishTime = new Date();
        String text = JSON.toJSONString(model);

        Model model2 = JSON.parseObject(text, Model.class);
        assertEquals(model.publishTime, model2.publishTime);
    }

    public static class Model {
        @JSONField(format= "yyyy-MM-dd'T'HH:mm:ss.SSS")
        public Date publishTime;
    }
}
