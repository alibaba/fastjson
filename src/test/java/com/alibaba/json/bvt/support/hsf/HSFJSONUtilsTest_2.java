package com.alibaba.json.bvt.support.hsf;

import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.alibaba.fastjson.support.hsf.MethodLocator;
import junit.framework.TestCase;
import net.minidev.json.JSONUtil;

import java.lang.reflect.Method;

public class HSFJSONUtilsTest_2 extends TestCase {
    public void test_for_hsf() throws Exception {
        final Method method = HSFJSONUtilsTest_2.class.getMethod("f", VeryComplexDO.class);

        String json = "{\n" +
                "\t\"argsTypes\":[\"com.alibaba.json.bvt.support.hsf.HSFJSONUtilsTest_2.VeryComplexDO\"],\n" +
                "\t\"argsObjs\":[\n" +
                "\t\t{\n" +
                "\t\t\t\"fshort\":2,\n" +
                "\t\t\t\"pBaseDO\":{\"id\":45},\n" +
                "\t\t\t\"pbyte\":3,\n" +
                "\t\t\t\"pfloat\":1.2,\n" +
                "\t\t\t\"pint\":69,\n" +
                "\t\t\t\"plist\":[\"taobao\",\"java\",\"linux\"],\n" +
                "\t\t\t\"plong\":56,\n" +
                "\t\t\t\"plongArray\":[1,2,3,4,5,6],\n" +
                "\t\t\t\"pmap\":{\"love\":\"taobao\",\"test\":\"HSF\",\"me\":\"you\"},\n" +
                "\t\t\t\"ptreeset\":[\"aaa\",\"bbb\"]\n" +
                "\t\t\t\n" +
                "\t\t}\t\t\n" +
                "\t]\n" +
                "}";

        HSFJSONUtils.parseInvocationArguments(json, new MethodLocator() {

            public Method findMethod(String[] types) {
                return method;
            }
        });

    }

    public static void f(VeryComplexDO vo) {

    }

    public static class VeryComplexDO {

    }
}
