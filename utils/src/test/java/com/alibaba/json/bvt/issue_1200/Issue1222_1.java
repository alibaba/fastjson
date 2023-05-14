package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 01/06/2017.
 */
public class Issue1222_1 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.type = Type.A;
        String text = JSON.toJSONString(model, SerializerFeature.WriteEnumUsingToString);
        assertEquals("{\"type\":\"TypeA\"}", text);
    }

    private static class Model {
        public Type type;
    }

    private static enum Type implements JSONAware {
        A, B;

        public String toJSONString() {
            return "\"Type" + this.name() + "\"";
        }
    }
}
