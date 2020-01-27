package com.alibaba.json.bvt.stream;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONWriter;

@SuppressWarnings("deprecation")
public class JSONWriterTest extends TestCase {

    public void test_0() throws Exception {
        StringWriter out = new StringWriter();

        JSONWriter writer = new JSONWriter(out);
        writer.writeStartObject();
        writer.writeEndObject();
        writer.flush();

        Assert.assertEquals("{}", out.toString());
    }

    public void test_1() throws Exception {
        StringWriter out = new StringWriter();

        JSONWriter writer = new JSONWriter(out);
        writer.writeStartObject();
        writer.writeKey("id");
        writer.writeValue(33);
        writer.writeEndObject();
        writer.flush();

        Assert.assertEquals("{\"id\":33}", out.toString());
    }

    public void test_2() throws Exception {
        StringWriter out = new StringWriter();

        JSONWriter writer = new JSONWriter(out);
        writer.writeStartObject();

        writer.writeKey("id");
        writer.writeValue(33);

        writer.writeKey("name");
        writer.writeValue("jobs");

        writer.writeEndObject();
        writer.flush();

        Assert.assertEquals("{\"id\":33,\"name\":\"jobs\"}", out.toString());
    }

    public void test_3() throws Exception {
        StringWriter out = new StringWriter();

        JSONWriter writer = new JSONWriter(out);
        writer.writeStartObject();

        writer.writeKey("id");
        writer.writeValue(33);

        writer.writeKey("name");
        writer.writeValue("jobs");

        writer.writeKey("children");
        writer.writeStartArray();

        writer.writeStartObject();
        writer.writeEndObject();

        writer.writeStartObject();
        writer.writeEndObject();

        writer.writeEndArray();

        writer.writeEndObject();
        writer.flush();

        Assert.assertEquals("{\"id\":33,\"name\":\"jobs\",\"children\":[{},{}]}", out.toString());
    }

    public void test_4() throws Exception {
        StringWriter out = new StringWriter();

        JSONWriter writer = new JSONWriter(out);

        writer.writeStartArray();

        writer.writeStartObject();
        writer.writeEndObject();

        writer.writeStartObject();
        writer.writeEndObject();

        writer.writeStartArray();
        writer.writeEndArray();
        {
            writer.writeStartArray();

            writer.writeStartArray();
            writer.writeEndArray();

            writer.writeStartArray();
            writer.writeEndArray();

            writer.writeEndArray();
            
            writer.writeValue(1);
        }

        writer.writeEndArray();

        writer.flush();

        Assert.assertEquals("[{},{},[],[[],[]],1]", out.toString());
    }

    /**
     * test for fix for #2602
     * @throws Exception
     */
    public void test_5() throws Exception {
		CMessage message = new CMessage();
        String orderMsgDTOJsonStr = "{" +
                "  \"msgEventDTO\": {" +
                "    \"msgEventType\": \"PAID\"" +
                "  }," +
                "  \"orderBaseDTO\": {" +
                "    \"buyerComment\": \"\"," +
                "    \"buyerUserId\": 1111111111," +
                "    \"cancelReason\": \"\"," +
                "    \"cancelTime\": 0," +
                "    \"created\": 1564624862," +
                "    \"distributorId\": 1111111112," +
                "    \"expiredTime\": 1569808909," +
                "    \"extra\": \"{\"orderSource\":\"0\",\"payType\":\"WX\",\"payChannelCode\":\"wechat\"," +
                "       \"distributorId\":\"aaaaaaaa\",\"newDefaultFlow\":\"1\",\"weshopSdkSource\":\"1\"," +
                "       \"shipExpenseCode\":\"defaultlogistics\",\"payChannel\":\"微信\",\"xcx\":\"1\"}\"," +
                "    \"extraInt\": 0," +
                "    \"itemId\": 0," +
                "    \"itemOrderExId\": 0," +
                "    \"level\": 1," +
                "    \"lockTime\": 0," +
                "    \"number\": 1," +
                "    \"orderFinishTime\": 0," +
                "    \"orderId\": 11111111111111," +
                "    \"outId\": \"\"," +
                "    \"parentOrderId\": 11111111111111," +
                "    \"parentOrderId1\": 11111111111111," +
                "    \"parentOrderId2\": 11111111111111," +
                "    \"parentOrderId3\": 11111111111111," +
                "    \"parentOrderId4\": 11111111111111," +
                "    \"parentOrderId5\": 11111111111111," +
                "    \"payId\": 1111111," +
                "    \"payTime\": 1564624909," +
                "    \"platformType\": 0," +
                "    \"price\": 1," +
                "    \"processType\": 0," +
                "    \"promotion\": \"[]\"," +
                "    \"promotionAmount\": 0," +
                "    \"rateFromBuyerVisible\": 0," +
                "    \"receiveTime\": 0," +
                "    \"receiveType\": -1," +
                "    \"refundStatus\": 0," +
                "    \"reverseStatus\": 0," +
                "    \"sellerComment\": \"\"," +
                "    \"sellerUserId\": 1111111111," +
                "    \"settlementTime\": 0," +
                "    \"shipExpense\": 0," +
                "    \"shipTime\": 0," +
                "    \"source\": \"\"," +
                "    \"status\": 2," +
                "    \"statusEx\": 0," +
                "    \"stockId\": 0," +
                "    \"type\": 12," +
                "    \"updated\": 1564624909," +
                "    \"visible\": 0" +
                "  }" +
                "}";
        // String orderMsgDTOJsonStr = "{" +
        //         "  \"msgEventDTO\": {" +
        //         "    \"msgEventType\": \"PAID\"" +
        //         "  }," +
        //         "  \"orderBaseDTO\": {" +
        //         "    \"buyerUserId\": 1," +
        //         "    \"extra\": \"{\\\"payChannel\\\":\\\"微信\\\"}\"" +
        //         "  }" +
        //         "}";
		byte[] orderMsgDTOBytes = new byte[0];
		try {
            orderMsgDTOBytes = orderMsgDTOJsonStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        message.setContent(orderMsgDTOBytes);

		JSONWriter writer = null;
		OutputStream out = new ByteArrayOutputStream();
		writer = new JSONWriter(new OutputStreamWriter(out,
                Charset.forName("UTF-8").newEncoder().onMalformedInput(CodingErrorAction.IGNORE)));
		writer.config(SerializerFeature.QuoteFieldNames, true);
		writer.config(SerializerFeature.SkipTransientField, true);
		writer.config(SerializerFeature.SortField, true);
		writer.config(SerializerFeature.WriteEnumUsingToString, false);
		//writer.config(SerializerFeature.NotWriteDefaultValue, true);
		writer.config(SerializerFeature.WriteClassName, true);
		writer.config(SerializerFeature.DisableCircularReferenceDetect, true);
		writer.writeObject(message);
		writer.flush();

		byte[] in = ((ByteArrayOutputStream) out).toByteArray();

		CMessage message1 = null;
		try {
			message1 = (CMessage) JSON.parse(in, 0, in.length,
                    Charset.forName("UTF-8").newDecoder(), Feature.AllowArbitraryCommas,
					Feature.IgnoreNotMatch, Feature.SortFeidFastMatch, Feature.DisableCircularReferenceDetect,
					Feature.AutoCloseSource, Feature.SupportAutoType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Assert.assertEquals(new String(message.content), new String(message1.content));
	}

	public static class CMessage implements Serializable {
		private static final long serialVersionUID = 1L;
        /**
         * 消息实体
         */
        private byte[] content;

        public byte[] getContent() {
            return content;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }
    }
}
