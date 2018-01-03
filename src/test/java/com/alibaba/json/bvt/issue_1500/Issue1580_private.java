package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import junit.framework.TestCase;

public class Issue1580_private extends TestCase {
    public void test_for_issue() throws Exception {
        SimplePropertyPreFilter classAFilter = new SimplePropertyPreFilter(Model.class, "code");
        SerializeFilter[] filters =new SerializeFilter[]{classAFilter};

        Model model = new Model();
        model.code = 1001;
        model.name = "N1";

        String json = JSON.toJSONString(model, filters, SerializerFeature.BeanToArray );
        assertEquals("[1001,null]", json);
    }

    private static class Model {
        private int code;
        private String name;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
