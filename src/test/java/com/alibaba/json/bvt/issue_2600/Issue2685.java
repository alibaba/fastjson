package com.alibaba.json.bvt.issue_2600;

import java.lang.reflect.Type;

import org.marre.sms.SmsMessage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.StringCodec;
import com.zx.sms.codec.cmpp.msg.CmppSubmitResponseMessage;
import com.zx.sms.codec.smgp.msg.SMGPSubmitMessage;
import com.zx.sms.common.util.CMPPCommonUtil;
import com.zx.sms.common.util.MsgId;

import junit.framework.TestCase;

public class Issue2685 extends TestCase {
    public void test_field() throws Exception {
        SMGPSubmitMessage smgpSubmitMessage = new SMGPSubmitMessage();
        smgpSubmitMessage.setSequenceNo(1);
        smgpSubmitMessage.setServiceId("hell");
        smgpSubmitMessage.setMsgContent("hello"); // 注释掉可以正常
        smgpSubmitMessage.setChargeTermId("123555");
        smgpSubmitMessage.setSrcTermId("10086");
        CmppSubmitResponseMessage submitResponseMessage = new CmppSubmitResponseMessage(1);
        submitResponseMessage.setResult(0);
        submitResponseMessage.setMsgId(new MsgId());

        String smsMsg = JSON.toJSONString(smgpSubmitMessage);
        // System.out.println(smsMsg);

        JSON.addMixInAnnotations(SMGPSubmitMessage.class, Mixin.class);
        smgpSubmitMessage = JSON.parseObject(smsMsg, SMGPSubmitMessage.class);
        assertEquals("hello", smgpSubmitMessage.getMsgContent());
    }

    public interface Mixin {
        @JSONField(deserializeUsing = MyDeserializer.class)
        void setMsgContent(SmsMessage msg);
    }

    public static class MyDeserializer implements ObjectDeserializer {

        @Override
        public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            String msg = StringCodec.deserialze(parser);
            return (T) CMPPCommonUtil.buildTextMessage(msg);
        }

        @Override
        public int getFastMatchToken() {
            return JSONToken.LITERAL_STRING;
        }

    }

}
