package com.alibaba.json.bvt.serializer.enum_;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 17/03/2017.
 */
public class EnumUsingToString_JSONType extends TestCase {
    public void test_toString() {
        Model model = new Model();
        model.gender = Gender.M;

        String text = JSON.toJSONString(model);
        assertEquals("{\"gender\":\"男\"}", text);
    }

    @JSONType(serialzeFeatures = SerializerFeature.WriteEnumUsingToString)
    public static class Model {
        public Gender gender;
    }

    public static enum Gender {
        M("男"),
        W("女");
        private String msg;

        Gender(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return msg;
        }
    }
}
