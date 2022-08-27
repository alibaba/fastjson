package com.alibaba.json.bvt.fullSer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvt.fullSer.get_set_Test.VO;

public class is_set_test_2 extends TestCase {

    public void test_codec() throws Exception {
        VO vo = new VO();
        vo.set_flag(true);

        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"flag\":true}", text);
        VO vo1 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(true, vo1.is_flag());
    }

    public static class VO {

        private boolean flag;

        public boolean is_flag() {
            return flag;
        }

        public void set_flag(boolean flag) {
            this.flag = flag;
        }

    }
}
