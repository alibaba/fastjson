package com.alibaba.json.bvt.bug;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_ascii_0_31 extends TestCase {

    public void test_0() throws Exception {
        for (int i = 0; i < 32; ++i) {
            StringBuilder buf = new StringBuilder();
            char ch = (char) i;
            buf.append(ch);

            String text = JSON.toJSONString(buf.toString(), SerializerFeature.BrowserCompatible);

            switch (ch) {
                case '"':
                    Assert.assertEquals("\"\\\"\"", text);
                    break;
                case '/':
                    Assert.assertEquals("\"\\/\"", text);
                    break;
                case '\\':
                    Assert.assertEquals("\"\\\\\"", text);
                    break;
                case '\b':
                    Assert.assertEquals("\"\\b\"", text);
                    break;
                case '\f':
                    Assert.assertEquals("\"\\f\"", text);
                    break;
                case '\n':
                    Assert.assertEquals("\"\\n\"", text);
                    break;
                case '\r':
                    Assert.assertEquals("\"\\r\"", text);
                    break;
                case '\t':
                    Assert.assertEquals("\"\\t\"", text);
                    break;
                default:
                    if (i < 16) {
                        Assert.assertEquals("\"\\u000" + Integer.toHexString(i).toUpperCase() + "\"", text);
                    } else {
                        Assert.assertEquals("\"\\u00" + Integer.toHexString(i).toUpperCase() + "\"", text);
                    }
                    break;
            }

            VO vo = new VO();
            vo.setContent(buf.toString());

            String voText = JSON.toJSONString(vo, SerializerFeature.BrowserCompatible);

            switch (ch) {
                case '"':
                    Assert.assertEquals("{\"content\":\"\\\"\"}", voText);
                    break;
                case '/':
                    Assert.assertEquals("{\"content\":\"\\/\"}", voText);
                    break;
                case '\\':
                    Assert.assertEquals("{\"content\":\"\\\\\"}", voText);
                    break;
                case '\b':
                    Assert.assertEquals("{\"content\":\"\\b\"}", voText);
                    break;
                case '\f':
                    Assert.assertEquals("{\"content\":\"\\f\"}", voText);
                    break;
                case '\n':
                    Assert.assertEquals("{\"content\":\"\\n\"}", voText);
                    break;
                case '\r':
                    Assert.assertEquals("{\"content\":\"\\r\"}", voText);
                    break;
                case '\t':
                    Assert.assertEquals("{\"content\":\"\\t\"}", voText);
                    break;
                default:
                    if (i < 16) {
                        Assert.assertEquals("{\"content\":\"\\u000" + Integer.toHexString(i).toUpperCase() + "\"}",
                                            voText);
                    } else {
                        Assert.assertEquals("{\"content\":\"\\u00" + Integer.toHexString(i).toUpperCase() + "\"}",
                                            voText);
                    }
                    break;
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
