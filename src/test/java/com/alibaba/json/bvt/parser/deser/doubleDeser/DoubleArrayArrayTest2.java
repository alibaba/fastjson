package com.alibaba.json.bvt.parser.deser.doubleDeser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 07/01/2017.
 */
public class DoubleArrayArrayTest2 extends TestCase {
    public void test_double_array() throws Exception {
        String text = "{\"matrix\":[[0.172129,0.723352],[0.177995,0.721544,0.182355,0.730706,0.176919,0.732701,0.186608,0.720192,0.190068,0.729511,0.192888]]}";
        Model model = JSON.parseObject(text, Model.class);
        String text2 = JSON.toJSONString(model);
        assertEquals(text, text2);
    }

    public static class Model {
        public double[][] matrix;
    }
}
