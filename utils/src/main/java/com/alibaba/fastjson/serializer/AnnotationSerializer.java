package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wenshao on 10/05/2017.
 */
public class AnnotationSerializer implements ObjectSerializer {
    private static volatile Class sun_AnnotationType = null;
    private static volatile boolean sun_AnnotationType_error = false;
    private static volatile Method sun_AnnotationType_getInstance = null;
    private static volatile Method sun_AnnotationType_members = null;

    public static AnnotationSerializer instance = new AnnotationSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        Class objClass = object.getClass();
        Class[] interfaces = objClass.getInterfaces();
        if (interfaces.length == 1 && interfaces[0].isAnnotation()) {
            Class annotationClass = interfaces[0];

            if (sun_AnnotationType == null && !sun_AnnotationType_error) {
                try {
                    sun_AnnotationType = Class.forName("sun.reflect.annotation.AnnotationType");
                } catch (Throwable ex) {
                    sun_AnnotationType_error = true;
                    throw new JSONException("not support Type Annotation.", ex);
                }
            }

            if (sun_AnnotationType == null) {
                throw new JSONException("not support Type Annotation.");
            }

            if (sun_AnnotationType_getInstance == null && !sun_AnnotationType_error) {
                try {
                    sun_AnnotationType_getInstance = sun_AnnotationType.getMethod("getInstance", Class.class);
                } catch (Throwable ex) {
                    sun_AnnotationType_error = true;
                    throw new JSONException("not support Type Annotation.", ex);
                }
            }

            if (sun_AnnotationType_members == null && !sun_AnnotationType_error) {
                try {
                    sun_AnnotationType_members = sun_AnnotationType.getMethod("members");
                } catch (Throwable ex) {
                    sun_AnnotationType_error = true;
                    throw new JSONException("not support Type Annotation.", ex);
                }
            }

            if (sun_AnnotationType_getInstance == null || sun_AnnotationType_error) {
                throw new JSONException("not support Type Annotation.");
            }

            Object type;
            try {
                type = sun_AnnotationType_getInstance.invoke(null, annotationClass);
            } catch (Throwable ex) {
                sun_AnnotationType_error = true;
                throw new JSONException("not support Type Annotation.", ex);
            }

            Map<String, Method> members;
            try {
                members = (Map<String, Method>) sun_AnnotationType_members.invoke(type);
            } catch (Throwable ex) {
                sun_AnnotationType_error = true;
                throw new JSONException("not support Type Annotation.", ex);
            }

            JSONObject json = new JSONObject(members.size());
            Iterator<Map.Entry<String, Method>> iterator = members.entrySet().iterator();
            Map.Entry<String, Method> entry;
            Object val = null;
            while (iterator.hasNext()) {
                entry = iterator.next();
                try {
                    val = entry.getValue().invoke(object);
                } catch (IllegalAccessException e) {
                    // skip
                } catch (InvocationTargetException e) {
                    // skip
                }
                json.put(entry.getKey(), JSON.toJSON(val));
            }
            serializer.write(json);
            return;
        }
    }
}
