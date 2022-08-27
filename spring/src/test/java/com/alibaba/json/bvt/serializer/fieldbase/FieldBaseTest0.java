package com.alibaba.json.bvt.serializer.fieldbase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import junit.framework.TestCase;

/**
 * Created by wenshao on 01/04/2017.
 */
public class FieldBaseTest0 extends TestCase {
    private static SerializeConfig config = new SerializeConfig(true);
    private static ParserConfig parserConfig = new ParserConfig(true);
    public void test_0() throws Exception {


        Model model = new Model();
        model.id = 123;
        assertEquals("{\"id\":123}", JSON.toJSONString(model, config));

        Model model2 = JSON.parseObject("{\"id\":123}", Model.class, parserConfig);
        assertEquals(model.id, model2.id);
    }

    public static class Model {
        private int id;
    }
}
