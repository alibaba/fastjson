package com.alibaba.json.bvt.support.hsf;

import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.alibaba.fastjson.support.hsf.MethodLocator;
import junit.framework.TestCase;

import java.lang.reflect.Method;
import java.util.List;

public class HSFJSONUtilsTest_1 extends TestCase {
    private Method method_f2;
    private Method method_f3;
    private Method method_f4;
    private Method method_f5;
    private MethodLocator methodLocator;

    protected void setUp() throws Exception {
        method_f2 = Service.class.getMethod("f2", String.class, Model.class);
        method_f3 = Service.class.getMethod("f3", String.class, List.class);
        method_f4 = Service.class.getMethod("f3", String.class, Model[].class);
        method_f5 = Service.class.getMethod("f3", int.class, long.class);

        methodLocator = new MethodLocator() {
            public Method findMethod(String[] types) {
                if (types == null || types.length == 0) {
                    return null;
                }

                if (types[0].equals("int")) {
                    return method_f5;
                }

                if (types[1].equals("java.util.List")) {
                    return method_f3;
                }

                if (types[1].equals("com.alibaba.json.bvt.support.hsf.HSFJSONUtilsTest_0$Model[]")) {
                    return method_f4;
                }

                return method_f2;
            }
        };
    }

    public void test_invoke() throws Exception {
        String json = "{ \n" +
                "    \"argsTypes\"  :  [ \"java.lang.String\", \"com.alibaba.json.bvt.support.hsf.HSFJSONUtilsTest_0$Model\"],\n" +
                "    \"argsObjs\"   :   [ \"abc\", {\"value\":\"xxx\"} ]\n" +
                "}";
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("abc", values[0]);
        assertEquals("xxx", ((Model) values[1]).value);
    }

    public void test_invoke_type() throws Exception {
        String json = "{\"@type\":\"com.alibaba.fastjson.JSONObject\", \n" +
                "    \"argsTypes\"  :  [ \"java.lang.String\", \"com.alibaba.json.bvt.support.hsf.HSFJSONUtilsTest_0$Model\"],\n" +
                "    \"argsObjs\"   :   [ \"abc\", {\"value\":\"xxx\"} ]\n" +
                "}";
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("abc", values[0]);
        assertEquals("xxx", ((Model) values[1]).value);
    }

    public void test_invoke_reverse() throws Exception {
        String json = "{ \n" +
                "    \"argsObjs\"   :   [ \"abc\", {\"value\":\"xxx\"} ],\n" +
                "    \"argsTypes\"  :  [ \"java.lang.String\", \"com.alibaba.json.bvt.support.hsf.HSFJSONUtilsTest_0$Model\"]\n" +
                "}";
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("abc", values[0]);
        assertEquals("xxx", ((Model) values[1]).value);
    }

    public void test_invoke_reverse_list() throws Exception {
        String json = "{ \n" +
                "    \"argsObjs\"   :   [ \"abc\", [{\"value\":\"xxx\"}] ],\n" +
                "    \"argsTypes\"  :  [ \"java.lang.String\", \"java.util.List\"]\n" +
                "}";
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("abc", values[0]);
        List list = (List) values[1];
        assertEquals("xxx", ((Model) list.get(0)).value);
    }

    public void test_invoke_reverse_array() throws Exception {
        String json = "{ \n" +
                "    \"argsObjs\"   :   [ \"abc\", [{\"value\":\"xxx\"}] ],\n" +
                "    \"argsTypes\"  :  [ \"java.lang.String\", \"com.alibaba.json.bvt.support.hsf.HSFJSONUtilsTest_0$Model[]\"]\n" +
                "}";
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("abc", values[0]);
        Model[] list = (Model[]) values[1];
        assertEquals("xxx", ((Model) list[0]).value);
    }

    public void test_invoke_array() throws Exception {
        String json = "[ \n" +
                "   [ \"java.lang.String\", \"com.alibaba.json.bvt.support.hsf.HSFJSONUtilsTest_0$Model\"],\n" +
                "    [ \"abc\", {\"value\":\"xxx\"} ]\n" +
                "]";
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("abc", values[0]);
        assertEquals("xxx", ((Model) values[1]).value);
    }

    public void test_invoke_array_2() throws Exception {
        String json = "[ \n" +
                "   [ \"java.lang.String\", \"java.util.List\"],\n" +
                "    [ \"abc\", [{\"value\":\"xxx\"}] ]\n" +
                "]";
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("abc", values[0]);

        List list = (List) values[1];
        assertEquals("xxx", ((Model) list.get(0)).value);
    }

    public void test_invoke_array_3() throws Exception {
        String json = "[ \n" +
                "   [ \"java.lang.String\", \"com.alibaba.json.bvt.support.hsf.HSFJSONUtilsTest_0$Model[]\"],\n" +
                "    [ \"abc\", [{\"value\":\"xxx\"}] ]\n" +
                "]";
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("abc", values[0]);

        Model[] list = (Model[]) values[1];
        assertEquals("xxx", ((Model) list[0]).value);
    }

    public void test_invoke_int() throws Exception {
        String json = "[ \n" +
                "   [ \"int\", \"long\"],\n" +
                "    [ 3,4 ]\n" +
                "]";
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals(3, ((Integer)values[0]).intValue());
        assertEquals(4L, ((Long)values[1]).longValue());
    }

    public void test_invoke_int_obj_reverse() throws Exception {
        String json = "{ \n" +
                "    \"argsObjs\"   :   [ 3, 4],\n" +
                "    \"argsTypes\"  :  [ \"int\", \"long\"]\n" +
                "}";
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals(3, ((Integer)values[0]).intValue());
        assertEquals(4L, ((Long)values[1]).longValue());
    }

    public void test_invoke_int_obj() throws Exception {
        String json = "{ \n" +
                "    \"argsTypes\"  :  [ \"int\", \"long\"],\n" +
                "    \"argsObjs\"   :   [ 3, 4 ]\n" +
                "}";
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals(3, ((Integer)values[0]).intValue());
        assertEquals(4L, ((Long)values[1]).longValue());
    }

    public void test_invoke_int_obj_2() throws Exception {
        String json = "{ \n" +
                "    \"argsObjs\"   :   [ 3, 4 ]\n" +
                "}";
        Object[] values = HSFJSONUtils.parseInvocationArguments(json,  new MethodLocator() {

            public Method findMethod(String[] types) {
                return method_f5;
            }
        });
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals(3, ((Integer)values[0]).intValue());
        assertEquals(4L, ((Long)values[1]).longValue());
    }

    public void test_invoke_int_2() throws Exception {
        String json = "[ \n" +
                "    null, [ 3,4 ]\n" +
                "]";
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, new MethodLocator() {

            public Method findMethod(String[] types) {
                return method_f5;
            }
        });
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals(3, ((Integer)values[0]).intValue());
        assertEquals(4L, ((Long)values[1]).longValue());
    }
//
    public static class Service {
        public void f2(String name, Model model) {

        }

        public void f3(String name, List<Model> models) {

        }

        public void f3(String name, Model[] models) {

        }

        public void f3(int a, long b) {

        }
    }

    public static class Model {
        public String value;
    }

    public static class User {
        public String name;
        public int id;
        public int age;
    }
}
