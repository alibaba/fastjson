package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.beans.Transient;

/**
 * Created by wenshao on 19/12/2016.
 */
public class Issue944 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.id = 1001;

        String text = JSON.toJSONString(model, SerializerFeature.SkipTransientField);

        assertEquals("{}", text);
    }

    public static class Model {
        private int id;

        @Transient
        public int getId() {
            return id;
        }
    }
}
