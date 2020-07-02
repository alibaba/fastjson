package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.StringCodec;
import junit.framework.TestCase;
import lombok.Data;

import java.lang.reflect.Type;

/**
 * @Author ：Nanqi
 * @Date ：Created in 22:21 2020/7/1
 */
public class Issue3306 extends TestCase {
    @Override
    protected void setUp() throws Exception {
        ParserConfig pc = ParserConfig.getGlobalInstance();
        StringCodecEx sc = new StringCodecEx();
        pc.putDeserializer(String.class, sc);
        pc.putDeserializer(StringBuilder.class, sc);
        pc.putDeserializer(StringBuffer.class, sc);
    }

    @Override
    protected void tearDown() throws Exception {
        ParserConfig pc = ParserConfig.getGlobalInstance();
        pc.clearDeserializers();
    }

    public void test_for_issue() throws Exception {
        UserOperateParam param = JSON.parseObject(
                "{\n" + "\"name\":\"nanqi   \",\n" + "\"phone\":\"15088XXX202   \",\n" + "\"skill\":\"java   \",\n"
                        + "\"recruitment\":\"4  \",\n" + "\"region\":\"Hangzhou   \", \"id\":1}",
                UserOperateParam.class);
        assertTrue("nanqi".equals(param.getName()));
        assertTrue("15088XXX202".equals(param.getPhone()));
        assertTrue("java".equals(param.getSkill()));
        assertTrue("4".equals(param.getRecruitment()));
        assertTrue("Hangzhou".equals(param.getRegion()));
        assertTrue(1 == param.getId());
    }

    public static class StringCodecEx extends StringCodec {
        /**
         * 对字符串作统一的归一化操作和trim操作。
         *
         * @author liliang
         * @since 2020-06-18 V2018C01
         */
        public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
            T t = super.deserialze(parser, clazz, fieldName);
            System.out.println("***** entry stringcodecex: " + fieldName + ":" + t + "----");
            if (t instanceof String) {
                return (T) trimAndNormalize((String) t);
            } else if (t instanceof StringBuilder) {
                return (T) trimAndNormalize((StringBuilder) t);
            } else if (t instanceof StringBuffer) {
                return (T) trimAndNormalize((StringBuffer) t);
            }
            return t;
        }

        /**
         * 对字符进行trim操作和归一化操作.
         *
         * @param str 要trim和归一化操作的字符串。
         * @return trim和归一化操作后的字符串。
         */
        public static String trimAndNormalize(String str) {
            if (str == null)
                return str;
            return str.trim();
        }

        /**
         * 对字符进行trim操作和归一化操作.
         *
         * @param str 要trim和归一化操作的字符串。
         * @return trim和归一化操作后的字符串。
         */
        public static StringBuilder trimAndNormalize(StringBuilder str) {
            return new StringBuilder(trimAndNormalize(str.toString()));
        }

        /**
         * 对字符进行trim操作和归一化操作.
         *
         * @param str 要trim和归一化操作的字符串。
         * @return trim和归一化操作后的字符串。
         */
        public static StringBuffer trimAndNormalize(StringBuffer str) {
            return new StringBuffer(trimAndNormalize(str.toString()));
        }
    }

    @Data
    public static class UserOperateParam {

        /**
         * 地域代码.
         */
        private String region;

        /**
         * 招聘顾问登录帐号.
         */
        private String recruitment;

        /**
         * 姓名.
         */
        private String name;

        /**
         * 电话号码.
         */
        private String phone;

        /**
         * 专业技能.
         */
        private String skill;

        private Integer id;
    }

}
