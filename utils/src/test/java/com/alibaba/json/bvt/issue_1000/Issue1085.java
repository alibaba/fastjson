package com.alibaba.json.bvt.issue_1000;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONType;
import junit.framework.TestCase;

/**
 * Created by wenshao on 20/03/2017.
 */
public class Issue1085 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = (Model) JSON.parseObject("{\"id\":123}", AbstractModel.class);
        assertEquals(123, model.id);
    }

    public static abstract class AbstractModel {
        public int id;

        @JSONCreator
        public static AbstractModel createInstance() {
            return new Model();
        }
    }

    public static class Model extends AbstractModel {
    }
}
