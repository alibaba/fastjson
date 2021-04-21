package com.alibaba.json.bvt.issue_3700;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SerializerFeatureWriteClassNameCharacter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Issue3725 {
    private static List<Object> list = new ArrayList<Object>();

    @BeforeClass
    public static void beforeClass() {
        final char charValue = 'S';
        final short shortValue = 1;
        final byte byteValue = 1;
        list.add("xxxx");
        list.add(1L);
        list.add(1);
        list.add(shortValue);
        list.add(byteValue);
        list.add(1F);
        list.add(1D);
        list.add(charValue);
        list.add(true);
    }

    @Before
    public void before() {
        JSONValidator.setWriteClassName(false);
        int count = 0;
        String format = "[%s%c]";
        for (String str : variables) {
            for (char ch = 'A', ch2 = 'a'; ch <= 'Z' && ch2 <= 'z'; ch++, ch2++) {
                if (!SerializerFeatureWriteClassNameCharacter.isNumberCharacter(ch)) {
                    test_strs_with_wrong_className[count] = String.format(format, str, ch);
                    count++;
                }
                if (!SerializerFeatureWriteClassNameCharacter.isNumberCharacter(ch2)) {
                    test_strs_with_wrong_className[count] = String.format(format, str, ch2);
                    count++;
                }
            }
        }
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            if (SerializerFeatureWriteClassNameCharacter.isNumberCharacter(ch)) {
                test_strs_with_wrong_className[count] = String.format(format, "", ch);
                count++;
            }
        }
    }

    private static final String test_str_with_classname = "[\"xxxx\",1L,1,1S,1B,1.0F,1.0D,\"S\",true]";
    private static final String test_str_without_classname = "[\"xxxx\",1,1,1,1,1.0,1.0,\"S\",true]";

    @Test
    public void test_for_issue_1_with_classname_should_config_then_can_pass() {
        JSONValidator.setWriteClassName(true);
        String outputjsonWithClassName = JSON.toJSONString(list, SerializerFeature.WriteClassName);
        Assert.assertEquals(test_str_with_classname, outputjsonWithClassName);
        JSONValidator from = JSONValidator.from(test_str_with_classname);
        boolean validate = from.validate();
        System.out.println(validate); //false in the 1st, should be true
        Assert.assertTrue(validate);
        List jsonObject = JSON.parseObject(test_str_with_classname, List.class);
        System.out.println(jsonObject);
        List<Object> jsonArray = JSON.parseArray(test_str_with_classname, new Type[]{
                String.class, long.class, int.class, short.class, byte.class,
                float.class, double.class, String.class, boolean.class
        });
        System.out.println(jsonArray);
    }

    @Test
    public void test_for_issue_2_withoutClassNameCanPass() {
        String outputjsonWithoutClassName = JSON.toJSONString(list);
        Assert.assertEquals(test_str_without_classname, outputjsonWithoutClassName);
        JSONValidator from = JSONValidator.from(test_str_without_classname);
        boolean validate = from.validate();
        System.out.println(validate); //false in the 1st, should be true
        Assert.assertTrue(validate);
        List jsonObject = JSON.parseObject(test_str_without_classname, List.class);
        System.out.println(jsonObject);
        List<Object> jsonArray = JSON.parseArray(test_str_without_classname, new Type[]{
                String.class, long.class, int.class, short.class, byte.class,
                float.class, double.class, String.class, boolean.class
        });
        System.out.println(jsonArray);
    }

    @Test
    public void test_for_issue_3_no_config_can_not_pass() {
        String outputjsonWithClassName = JSON.toJSONString(list, SerializerFeature.WriteClassName);
        Assert.assertEquals(test_str_with_classname, outputjsonWithClassName);
        JSONValidator from = JSONValidator.from(test_str_with_classname);
        boolean validate = from.validate();
        System.out.println(validate); //false in the 1st, should be true
        Assert.assertFalse(validate);
    }

    private static final String[] variables = new String[]{"3725", "37.25", "0", "3.725e-2", "0.372e+5", ""};
    private static final String[] test_strs_with_wrong_className = new String[(2 * 26 - 5) * variables.length + 5];

    @Test
    public void test_for_issue_wrong_should_not_pass() {
        for (String str : test_strs_with_wrong_className) {
            JSONValidator from = JSONValidator.from(str);
            Assert.assertFalse(from.validate());
        }
    }
}
