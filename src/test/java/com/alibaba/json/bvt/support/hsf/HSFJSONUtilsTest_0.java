package com.alibaba.json.bvt.support.hsf;

import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.alibaba.fastjson.support.hsf.MethodLocator;
import junit.framework.TestCase;

import java.lang.reflect.Method;
import java.util.List;

public class HSFJSONUtilsTest_0 extends TestCase {
    private Method method_f2;
    private Method method_f3;
    private Method method_f4;
    private Method method_f5;
    private MethodLocator methodLocator;

    protected void setUp() throws Exception {
        method_f2 = Service.class.getMethod("f2", String.class, Model.class);
        method_f3 = Service.class.getMethod("f3", String.class, List.class);
        method_f4 = Service.class.getMethod("f4", List.class);
        method_f5 = Service.class.getMethod("f4", User[].class);

        methodLocator = new MethodLocator() {
            public Method findMethod(String[] types) {
                if (types == null) {
                    return method_f2;
                }

                if (types.length == 1 && types[0].equals("java.util.List")) {
                    return method_f4;
                }

                if (types.length == 1 && types[0].equals("com.alibaba.json.bvt.support.hsf.HSFJSONUtilsTest_0.User[]")) {
                    return method_f5;
                }

                if (types[1].equals("java.util.List")) {
                    return method_f3;
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

    public void test_invoke_1() throws Exception {
        String json = "{ \n" +
                "    \"argsObjs\"   :   [ \"abc\", {\"value\":\"xxx\"} ]\n" +
                "}";
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("abc", values[0]);
        assertEquals("xxx", ((Model) values[1]).value);
    }

    public void test_invoke_null() throws Exception {
        String json = "{ \n" +
                "    \"argsTypes\"  :  [ \"java.lang.String\", \"com.alibaba.json.bvt.support.hsf.HSFJSONUtilsTest_0$Model\"],\n" +
                "    \"argsObjs\"   :   [ null, null ]\n" +
                "}";
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals(null, values[0]);
        assertEquals(null, values[1]);
    }

    public void test_invoke_list() throws Exception {
        String json = "{ \n" +
                "    \"argsTypes\"  :  [ \"java.lang.String\", \"java.util.List\"],\n" +
                "    \"argsObjs\"   :   [ \"abc\", [" +
                "{" +
                "   \"value\":\"xxx\"" +
                "   }] ]\n" +
                "}";
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("abc", values[0]);

        List list = (List) values[1];
        assertEquals("xxx", ((Model) list.get(0)).value);
    }

    public void test_invoke_list_f4() throws Exception {
        String json = "{\n" +
                "    \"argsTypes\": [\"java.util.List\"],\n" +
                "    \n" +
                "    \"argsObjs\": [\n" +
                "        [\n" +
                "    \t\t{\n" +
                "    \t\t\t\"name\": \"123\",\n" +
                "    \t\t\t\"id\": 123,\n" +
                "    \t\t\t\"age\": 123\n" +
                "    \t\t},\n" +
                "    \t\t{\n" +
                "    \t\t\t\"name\": \"123\",\n" +
                "    \t\t\t\"id\": 123,\n" +
                "    \t\t\t\"age\": 123\n" +
                "    \t\t}\n" +
                "\t\t]\n" +
                "    ]\n" +
                "}";
//        System.out.println(json);
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(1, values.length);

        List list = (List) values[0];
        assertEquals("123", ((User) list.get(0)).name);
        assertEquals("123", ((User) list.get(1)).name);
    }

    public void test_invoke_list_f5() throws Exception {
        String json = " [\n" +
                " \t[\"java.util.List\"],\n" +
                "    [\n" +
                "    \t\t[{\n" +
                "    \t\t\t\"name\": \"123\",\n" +
                "    \t\t\t\"id\": 123,\n" +
                "    \t\t\t\"age\": 123\n" +
                "    \t\t},\n" +
                "    \t\t{\n" +
                "    \t\t\t\"name\": \"123\",\n" +
                "    \t\t\t\"id\": 123,\n" +
                "    \t\t\t\"age\": 123\n" +
                "    \t\t}]\n" +
                "    ]\n" +
                "]";
        System.out.println(json);
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(1, values.length);

        List list = (List) values[0];
        assertEquals("123", ((User) list.get(0)).name);
        assertEquals("123", ((User) list.get(1)).name);
    }

    public void test_invoke_array() throws Exception {
        String json = "{\n" +
                "    \"argsTypes\": [\"com.alibaba.json.bvt.support.hsf.HSFJSONUtilsTest_0.User[]\"],\n" +
                "    \n" +
                "    \"argsObjs\": [\n" +
                "        [\n" +
                "    \t\t{\n" +
                "    \t\t\t\"name\": \"123\",\n" +
                "    \t\t\t\"id\": 123,\n" +
                "    \t\t\t\"age\": 123\n" +
                "    \t\t},\n" +
                "    \t\t{\n" +
                "    \t\t\t\"name\": \"123\",\n" +
                "    \t\t\t\"id\": 123,\n" +
                "    \t\t\t\"age\": 123\n" +
                "    \t\t}\n" +
                "\t\t]\n" +
                "    ]\n" +
                "}";
//        System.out.println(json);
        Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        assertNotNull(values);
        assertEquals(1, values.length);

        User[] list = (User[]) values[0];
        assertEquals("123", ((User) list[0]).name);
        assertEquals("123", ((User) list[1]).name);
    }

//    public void test_perf() throws Exception {
//        for (int i = 0; i < 5; ++i) {
//            perf(); // 723
//        }
//    }

    void perf() throws Exception {
        long start = System.currentTimeMillis();
        String json = "{ \n" +
                "    \"argsTypes\"  :  [ \"java.lang.String\", \"com.alibaba.json.bvt.support.hsf.HSFJSONUtilsTest_0$Model\"],\n" +
                "    \"argsObjs\"   :   [ \"abc\", {\"value\":\"xxx\"} ]\n" +
                "}";
        for (int i = 0; i < 1000 * 1000; ++i) {
            Object[] values = HSFJSONUtils.parseInvocationArguments(json, methodLocator);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static class Service {
        public void f1() {

        }

        public void f2(String name, Model model) {

        }

        public void f3(String name, List<Model> models) {

        }

        public void f4( List<User> models) {

        }

        public void f4( User[] models) {

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
