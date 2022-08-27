package com.alibaba.json.bvt.serializer.enum_;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 17/03/2017.
 */
public class EnumOrdinalTest extends TestCase {
    public void test_enum_ordinal() throws Exception {
        Model model = new Model();
        model.type = Type.Big;

        int serializerFeatures = JSON.DEFAULT_GENERATE_FEATURE & ~SerializerFeature.WriteEnumUsingName.mask;
        String text = JSON.toJSONString(model, serializerFeatures);
        System.out.println(text);
    }

    public static class Model {
        public Type type;
    }

    public static enum Type {
        Big, Medium, Small
    }
}
