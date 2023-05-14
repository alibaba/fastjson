package com.alibaba.json.bvt.serializer.enum_;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

public class EnumTest4 extends TestCase {
    public void test_for_enum() throws Exception {
        assertEquals("101", JSON.toJSONString(Type.Big));
        assertEquals("101", JSON.toJSONString(Type1.Big));
    }

    public enum Type {
        Big(101), Small(102);

        @JSONField
        public final int code;

        Type(int code) {
            this.code = code;
        }
    }

    public enum Type1 {
        Big(101), Small(102);

        private final int code;

        @JSONField
        public int getCode() {
            return code;
        }

        Type1(int code) {
            this.code = code;
        }
    }
}
