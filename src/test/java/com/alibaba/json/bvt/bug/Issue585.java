package com.alibaba.json.bvt.bug;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

import junit.framework.TestCase;

public class Issue585 extends TestCase {
    private String original = JSON.DEFAULT_TYPE_KEY;
    private ParserConfig originalConfig = ParserConfig.global;

    protected void setUp() throws Exception {
        ParserConfig.global = new ParserConfig();
        if (!JSON.DEFAULT_TYPE_KEY.equals("mySpace")) {
            JSON.setDefaultTypeKey("mySpace");
        }
    }

    protected void tearDown() throws Exception {
        JSON.setDefaultTypeKey(original);
        ParserConfig.global = originalConfig;
    }

    public void test_for_issue() throws Exception {

        String cc = "{\"mySpace\":\"com.alibaba.json.bvt.bug.Issue585$Result\",\"attachments\":{\"mySpace\":\"java.util.HashMap\",\"timeout\":5000,\"consumeApp\":\"multiGroupTestServer\"},\"status\":0}";

        byte[] bytes = cc.getBytes("utf-8");

        Result res = (Result) this.deserialize(bytes);
        Assert.assertEquals(0, res.getStatus());
    }

    public <T> T deserialize(byte[] in) {
        Charset CHARSET = Charset.forName("utf-8");
        return (T) JSON.parse(in, 0, in.length, CHARSET.newDecoder(), Feature.AllowArbitraryCommas,
                              Feature.IgnoreNotMatch, Feature.SortFeidFastMatch, Feature.DisableCircularReferenceDetect,
                              Feature.AutoCloseSource);
    }

    public static class Result {

        private int                 status;
        private Object              value;
        private Map<String, Object> attachments = new HashMap<String, Object>(2);

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Map<String, Object> getAttachments() {
            return attachments;
        }

        public void setAttachments(Map<String, Object> attachments) {
            this.attachments = attachments;
        }
    }
}
