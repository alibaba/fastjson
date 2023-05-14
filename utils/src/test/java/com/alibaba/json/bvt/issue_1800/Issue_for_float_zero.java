package com.alibaba.json.bvt.issue_1800;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class Issue_for_float_zero extends TestCase {
    public void test_0() throws Exception {
        M1 m = new M1(1.0f);
        assertEquals("{\"val\":1.0}", JSON.toJSONString(m, SerializerFeature.WriteNullNumberAsZero));
    }

    public void test_1() throws Exception {
        M2 m = new M2(1.0);
        assertEquals("{\"val\":1.0}", JSON.toJSONString(m, SerializerFeature.WriteNullNumberAsZero));
    }

    public static class M1 {
        public float val;

        public M1(float val) {
            this.val = val;
        }
    }

    public static class M2 {
        public double val;

        public M2(double val) {
            this.val = val;
        }
    }
}
