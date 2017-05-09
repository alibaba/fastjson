package com.alibaba.json.bvt.issue_1100;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 10/04/2017.
 */
public class Issue1138 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.id = 1001;
        model.name = "gaotie";

        // {"id":1001,"name":"gaotie"}
        String text_normal = JSON.toJSONString(model);
        System.out.println(text_normal);

        // [1001,"gaotie"]
        String text_beanToArray = JSON.toJSONString(model,
                SerializerFeature.BeanToArray);
        System.out.println(text_beanToArray);

        // support beanToArray & normal mode
        System.out.println(JSON.parseObject(text_beanToArray, Model.class, Feature.SupportArrayToBean));
    }

    static class Model {
        public int id;
        public String name;
    }

}
