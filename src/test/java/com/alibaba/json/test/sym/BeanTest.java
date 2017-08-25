package com.alibaba.json.test.sym;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sym on 17/8/24.
 */
public class BeanTest extends TestCase {


    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    public void test() {

       /* {
            Utils.printLn(2);
        }*/
        Person person = new Person();
        /*person.age = 27;
        person.name = "sym";*/

        /*ValueFilter valueFilter = new ValueFilter() {

            public Object process(Object object, String name, Object value) {
                try {
                    Field field = object.getClass().getField(name);
                    Class<?> clazz = field.getType();

                    if (clazz == String.class) {
                        if (value == null) {
                            return "sym";
                        }
                    } else if (clazz == Integer.TYPE) {
                        return 101;
                    } else if (clazz == Boolean.TYPE) {
                        return true;
                    } else if (clazz == Long.TYPE) {
                        return 1001.11;
                    } else if (clazz == ArrayList.class) {
                        value = new ArrayList<Person.Child>();
                        List<Person.Child> list = (List<Person.Child>) value;
                        for (int i = 0; i < 5; i++) {
                            Person.Child child = new Person.Child();
                            list.add(child);
                        }

                        return value;
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }


                return null;
            }
        };*/

        //String p = JSON.toJSONString(person, valueFilter, SerializerFeature.WriteMapNullValue);
        String json = generateTestData(Person.class);

        Utils.printLn(json);

        Person person1 = JSON.parseObject(json, Person.class);
        Utils.printLn(person1.age);

    }

    /**
     * 生成测试数据
     *
     * @return
     */
    public static <E> String generateTestData(final Class<E> clazz1) {

        String json = "";
        try {
            E instance = clazz1.newInstance();

            ValueFilter valueFilter = new ValueFilter() {

                public Object process(Object object, String name, Object value) {
                    try {

                        Utils.printLn(object.getClass().getSimpleName());

                        Field field = object.getClass().getField(name);
                        Class<?> clazz = field.getType();

                        if (clazz == String.class) {
                            if (value == null) {
                                return "sym";
                            }
                        } else if (clazz == Integer.TYPE) {

                            return 101;
                        } else if (clazz == Boolean.TYPE) {

                            if (value == null) {
                                return true;
                            }

                        } else if (clazz == Long.TYPE) {

                            return 1001.11;
                        } else {
                            if (clazz == ArrayList.class) {

                                Type genericType = field.getGenericType();
                                if (genericType == null) {
                                    return null;
                                }

                                Class genericClazz = null;
                                if (genericType instanceof ParameterizedType) {
                                    ParameterizedType pt = (ParameterizedType) genericType;
                                    genericClazz = (Class) pt.getActualTypeArguments()[0];
                                }
                                value = new ArrayList();
                                List list = (List) value;
                                for (int i = 0; i < 5; i++) {
                                    Object child = genericClazz.newInstance();
                                    list.add(child);
                                }

                                return value;
                            }
                        }

                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }


                    return value;
                }
            };

            json = JSON.toJSONString(instance, valueFilter, SerializerFeature.WriteMapNullValue);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return json;
    }
}
