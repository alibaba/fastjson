package com.alibaba.json.bvt.bug;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_ascii_0_31 extends TestCase {

    public void test_0() throws Exception {
        for (int i = 0; i < 32; ++i) {
            StringBuilder buf = new StringBuilder();
            char c = (char) i;
            buf.append(c);
            
            String text = JSON.toJSONString(buf.toString(), SerializerFeature.BrowserCompatible);
            if (i < 16) {
                Assert.assertEquals("\"\\u000" + Integer.toHexString(i).toUpperCase() + "\"", text);
            } else {
                Assert.assertEquals("\"\\u00" + Integer.toHexString(i).toUpperCase() + "\"", text);
            }
            
            VO vo = new VO();
            vo.setContent(buf.toString());
            
            String voText = JSON.toJSONString(vo, SerializerFeature.BrowserCompatible);
            if (i < 16) {
                Assert.assertEquals("{\"content\":\"\\u000" + Integer.toHexString(i).toUpperCase() + "\"}", voText);
            } else {
                Assert.assertEquals("{\"content\":\"\\u00" + Integer.toHexString(i).toUpperCase() + "\"}", voText);
            }
        }

    }

    public static class VO {

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }
}
