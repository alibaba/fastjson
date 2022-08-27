package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import org.junit.Assert;

public class JSONFieldTest_unwrapped_0 extends TestCase {

    public void test_jsonField() throws Exception {
        VO vo = new VO();
        vo.id = 123;
        vo.localtion = new Localtion(127, 37);

        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"id\":123,\"latitude\":37,\"longitude\":127}", text);

        VO vo2 = JSON.parseObject(text, VO.class);
        assertNotNull(vo2.localtion);
        assertEquals(vo.localtion.latitude, vo2.localtion.latitude);
        assertEquals(vo.localtion.longitude, vo2.localtion.longitude);
    }

    public static class VO {
        public int id;

        @JSONField(unwrapped = true)
        public Localtion localtion;
    }

    public static class Localtion {
        public int longitude;
        public int latitude;

        public Localtion() {

        }

        public Localtion(int longitude, int latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }
}
