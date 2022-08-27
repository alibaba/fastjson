package com.alibaba.json.bvt.issue_1000;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenshao on 25/03/2017.
 */
public class Issue1066 extends TestCase {
    private static final Charset CHARSET = Charset.forName("UTF-8");

    public void test_for_issue() throws Exception {
        Map<EnumType, EnumType> map = new HashMap<EnumType, EnumType>();
        map.put(EnumType.ONE, EnumType.TWO);

        System.out.println("序列化前的参数为:" + map);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            serialize(map, bos);

            Object desRes = deserialize(bos.toByteArray());
            System.out.println("反序列化后的结果为:" + JSON.toJSONString(desRes));
        }finally {
            try{
                bos.close();
            }catch (IOException e){
            }
        }
    }

    public static <T> void serialize(T obj, OutputStream out) {
        JSONWriter writer = null;
        try {
            writer = new JSONWriter(new OutputStreamWriter(out, CHARSET.newEncoder().onMalformedInput(CodingErrorAction.IGNORE)));
            writer.config(SerializerFeature.QuoteFieldNames, true);
            writer.config(SerializerFeature.SkipTransientField, true);
            writer.config(SerializerFeature.SortField, true);
            writer.config(SerializerFeature.WriteClassName, true);
            writer.config(SerializerFeature.DisableCircularReferenceDetect, true);
            writer.writeObject(obj);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static <T> T deserialize(byte[] in) {
        return (T) JSON.parse(in, 0, in.length, CHARSET.newDecoder(), Feature.AllowArbitraryCommas,
                Feature.IgnoreNotMatch, Feature.SortFeidFastMatch, Feature.DisableCircularReferenceDetect,
                Feature.AutoCloseSource);
    }

    public static enum EnumType {

        ONE(1, "1"),

        TWO(2, "2")
        ;

        private int code;
        private String desc;

        EnumType(int code, String desc){
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String toString() {
            return "EnumType{" +
                    "code=" + code +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }


}
