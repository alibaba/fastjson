package com.alibaba.json.bvt.issue_2000;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.List;

public class Issue2066 extends TestCase {


    public void test_issue() throws Exception {
        JSON.parseObject("{\"values\":[[1,2],[3,4]]}", Model.class);
    }

    public static class Model {
        private List<float[]> values;

        public List<float[]> getValues() {
            return values;
        }

        public void setValues(List<float[]> values) {
            this.values = values;
        }
    }
}
