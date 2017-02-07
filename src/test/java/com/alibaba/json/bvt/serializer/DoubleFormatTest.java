package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.sun.javafx.sg.prism.NGShape;
import junit.framework.TestCase;

import java.text.DecimalFormat;

/**
 * Created by wenshao on 09/01/2017.
 */
public class DoubleFormatTest extends TestCase {
    public void test_format() throws Exception {
        Model model = new Model();
        model.value = 123.45678D;

        String str = JSON.toJSONString(model);
        assertEquals("{\"value\":123.46}", str);
    }

    public static class Model {
        @JSONField(format = "0.00")
        public double value;
    }
}
