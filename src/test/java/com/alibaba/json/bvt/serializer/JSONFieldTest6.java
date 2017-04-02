package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import org.junit.Assert;

public class JSONFieldTest6 extends TestCase {

    public void test_jsonField() throws Exception {
        VO vo = new VO();

        vo.localtion = new Localtion(127, 37);

        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"latitude\":37,\"longitude\":127}", text);

    }

    public static class VO {
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
