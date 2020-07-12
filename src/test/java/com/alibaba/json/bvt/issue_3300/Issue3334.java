package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue3334 extends TestCase {
    public void test_for_issue() throws Exception {
        assertEquals(0,
                JSON.parseObject("{\"id\":false}", VO.class).id);

        assertEquals(1,
                JSON.parseObject("{\"id\":true}", VO.class).id);


        assertEquals(0,
                JSON.parseObject("{\"id64\":false}", VO.class).id64);

        assertEquals(1,
                JSON.parseObject("{\"id64\":true}", VO.class).id64);

        assertEquals(0,
                JSON.parseObject("{\"id16\":false}", VO.class).id16);

        assertEquals(1,
                JSON.parseObject("{\"id16\":true}", VO.class).id16);


        assertEquals(0,
                JSON.parseObject("{\"id8\":false}", VO.class).id8);

        assertEquals(1,
                JSON.parseObject("{\"id8\":true}", VO.class).id8);


        assertEquals(0F,
                JSON.parseObject("{\"floatValue\":false}", VO.class).floatValue);

        assertEquals(1F,
                JSON.parseObject("{\"floatValue\":true}", VO.class).floatValue);

        assertEquals(0D,
                JSON.parseObject("{\"doubleValue\":false}", VO.class).doubleValue);

        assertEquals(1D,
                JSON.parseObject("{\"doubleValue\":true}", VO.class).doubleValue);
    }

    public static class VO {
        private byte id8;
        private short id16;
        private int id;
        private long id64;
        private Float floatValue;
        private Double doubleValue;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getId64() {
            return id64;
        }

        public void setId64(long id64) {
            this.id64 = id64;
        }

        public short getId16() {
            return id16;
        }

        public void setId16(short id16) {
            this.id16 = id16;
        }

        public byte getId8() {
            return id8;
        }

        public void setId8(byte id8) {
            this.id8 = id8;
        }

        public Float getFloatValue() {
            return floatValue;
        }

        public void setFloatValue(Float floatValue) {
            this.floatValue = floatValue;
        }

        public Double getDoubleValue() {
            return doubleValue;
        }

        public void setDoubleValue(Double doubleValue) {
            this.doubleValue = doubleValue;
        }
    }
}
