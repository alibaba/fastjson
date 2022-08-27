package com.alibaba.json.bvt.parser.autoType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

/**
 * Created by wenshao on 10/02/2017.
 */
public class AutoTypeTest1 extends TestCase {
    public void test_0() throws Exception {
        ParserConfig config = new ParserConfig();
        config.setAutoTypeSupport(true);

        System.out.println(Model.class.isAnnotationPresent(JSONField.class));
        String text = "{\"@type\":\"com.alibaba.json.bvt.parser.autoType.AutoTypeTest1$Model\",\"id\":123}";

        Model model2 = (Model) JSON.parseObject(text, Object.class, config);
        assertEquals(123, model2.id);
    }

    @JSONType
    public static class Model {
        public int id;
    }
}
