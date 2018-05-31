package com.alibaba.json.bvt.taobao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class ItemUpdateDOTest extends TestCase {

    public void test_1() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.setAsmEnable(false);
        Model item = new Model();
        JSON.toJSONString(item, config, SerializerFeature.IgnoreErrorGetter,
                SerializerFeature.IgnoreNonFieldGetter, SerializerFeature.WriteClassName,
                SerializerFeature.WriteMapNullValue);

        System.out.println(JSON.toJSONString("\u000B"));
    }

    public static class Model {
        private long f0 = 1;
        private long f1;

        public long getF0() {
            return f0;
        }

        public void setF0(long f0) {
            this.f0 = f0;
        }

        public long getF1() {
            return f1;
        }

        public void setF1(long f1) {
            this.f1 = f1;
        }

        /** @deprecated */
        @Deprecated
        public long getUpdateFeatureCc() {
            throw new IllegalArgumentException("updateFeatureCc不再使用");
        }
    }
}
