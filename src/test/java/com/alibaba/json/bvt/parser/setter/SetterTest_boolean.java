package com.alibaba.json.bvt.parser.setter;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import junit.framework.TestCase;

public class SetterTest_boolean extends TestCase {

    public void test_codec_false() throws Exception {
        VO vo = new VO();
        vo.id = false;

        String str = JSON.toJSONString(vo);

        VO vo1 = JSON.parseObject(str, VO.class);

        Assert.assertTrue(vo1.id == vo.id);
    }
    
    public void test_codec_true() throws Exception {
        VO vo = new VO();
        vo.id = true;

        String str = JSON.toJSONString(vo);

        VO vo1 = JSON.parseObject(str, VO.class);

        Assert.assertTrue(vo1.id == vo.id);
    }

    public static class VO {

        private boolean id;

        public boolean isId() {
            return id;
        }

        public void setId(boolean id) {
            this.id = id;
        }

    }
}
