package demo.annotations;

import com.alibaba.fastjson.JSON;
import sun.reflect.annotation.AnnotationType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Helly on 2017/04/10.
 */
public class AnnotationTest {

    public static void main(String[] args) {
        print();

        print2();

    }

    private static void print2() {
        PersonInfo info = Bob.class.getAnnotation(PersonInfo.class);
        Class<?> clazz = info.getClass();
        Class[] interfaces = clazz.getInterfaces();
        if (interfaces.length == 1 && interfaces[0].isAnnotation()) {
            AnnotationType type = AnnotationType.getInstance(interfaces[0]);
            Map<String, Method> members = type.members();
            Iterator<Map.Entry<String, Method>> iterator = members.entrySet().iterator();
            Map.Entry<String, Method> entry;
            Object val;
            while (iterator.hasNext()) {
                entry = iterator.next();
                try {
                    val = entry.getValue().invoke(info);
                    System.out.println(entry.getKey() + "=" + val);
                } catch (IllegalAccessException e) {
                    //skip
                } catch (InvocationTargetException e) {
                    //skip
                }
            }
        }
    }

    private static void print() {
        Bob bob = new Bob("Bob", 30, true);
        Object obj = JSON.toJSON(bob);
        System.out.println(obj.toString());
        PersonInfo info = Bob.class.getAnnotation(PersonInfo.class);
        obj = JSON.toJSON(info);
        System.out.println(obj.toString());
    }
}
