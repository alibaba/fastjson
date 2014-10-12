package com.alibaba.fastjson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.serializer.ASMJavaBeanSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;

/**
 * @author wenshao[szujobs@hotmail.com]
 * @since 1.2.0
 */
public class JSONPath {

    private final String    path;
    private Segement[]      pathSegments;

    private SerializeConfig serializeConfig;

    public JSONPath(String path){
        this(path, SerializeConfig.getGlobalInstance());
    }

    public JSONPath(String path, SerializeConfig serializeConfig){
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.path = path;
        this.serializeConfig = serializeConfig;
    }

    public Object eval(Object rootObject) {
        if (rootObject == null) {
            return null;
        }

        this.pathSegments = explain(path);

        Object currentObject = rootObject;
        for (int i = 0; i < pathSegments.length; ++i) {
            currentObject = pathSegments[i].eval(this, rootObject, currentObject);
        }
        return currentObject;
    }

    public String getPath() {
        return path;
    }

    public Segement[] explain(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Segement[] segements = new Segement[16];
        int len = 0;
        int beginIndex = 0;
        for (int i = 0; i < path.length(); ++i) {
            char ch = path.charAt(i);

            if (ch == '.') {
                segements[len++] = buildSegement(len, path.substring(beginIndex, i));
                beginIndex = i + 1;
            } else if (ch == '[') {
                segements[len++] = buildSegement(len, path.substring(beginIndex, i));
                beginIndex = i;
            } else if (i == path.length() - 1) {
                segements[len++] = buildSegement(len, path.substring(beginIndex, path.length()));
            }
        }

        if (len == segements.length) {
            return segements;
        }

        Segement[] result = new Segement[len];
        System.arraycopy(segements, 0, result, 0, len);
        return result;
    }

    Segement buildArraySegement(int level, String indexText) {
        int commaIndex = indexText.indexOf(',');

        if (indexText.length() > 2 && indexText.charAt(0) == '\'' //
            && indexText.charAt(indexText.length() - 1) == '\'') {

            if (commaIndex == -1) {
                String propertyName = indexText.substring(1, indexText.length() - 1);
                return new PropertySegement(propertyName);
            }

            String[] indexesText = indexText.split(",");
            String[] propertyNames = new String[indexesText.length];
            for (int i = 0; i < indexesText.length; ++i) {
                String indexesTextItem = indexesText[i];
                propertyNames[i] = indexesTextItem.substring(1, indexesTextItem.length() - 1);
            }

            return new MultiPropertySegement(propertyNames);
        }

        int colonIndex = indexText.indexOf(':');
        if (commaIndex == -1 && colonIndex == -1) {
            int index = Integer.parseInt(indexText);
            return new ArrayAccessSegement(index);
        }

        if (commaIndex != -1) {
            String[] indexesText = indexText.split(",");
            int[] indexes = new int[indexesText.length];
            for (int i = 0; i < indexesText.length; ++i) {
                indexes[i] = Integer.parseInt(indexesText[i]);
            }
            return new MultiArrayAccessSegement(indexes);
        }

        if (colonIndex != -1) {
            String[] indexesText = indexText.split(":");
            int[] indexes = new int[indexesText.length];
            for (int i = 0; i < indexesText.length; ++i) {
                indexes[i] = Integer.parseInt(indexesText[i]);
            }

            int start = indexes[0];
            int end = indexes[1];
            int step;
            if (indexes.length == 3) {
                step = indexes[2];
            } else {
                step = 1;
            }

            if (end < start) {
                throw new UnsupportedOperationException("end must greater than or equals start. start " + start
                                                        + ",  end " + end);
            }

            if (step <= 0) {
                throw new UnsupportedOperationException("step must greater than zero : " + step);
            }
            return new RangeArrayAccessSegement(start, end, step);
        }

        throw new UnsupportedOperationException();
    }

    Segement buildSegement(int level, String pathSegement) {
        if ("@".equals(pathSegement)) {
            return SelfSegement.instance;
        }

        if ("$".equals(pathSegement)) {
            return RootSegement.instance;
        }

        if ("*".equals(pathSegement)) {
            return WildCardSegement.instance;
        }

        if ("length()".equals(pathSegement) || "size()".equals(pathSegement)) {
            return SizeSegement.instance;
        }

        if (pathSegement.charAt(0) == '[' && pathSegement.charAt(pathSegement.length() - 1) == ']') {
            String indexText = pathSegement.substring(1, pathSegement.length() - 1);
            return buildArraySegement(level, indexText);
        }

        return new PropertySegement(pathSegement);
    }

    static interface Segement {

        Object eval(JSONPath path, Object rootObject, Object currentObject);
    }

    static class RootSegement implements Segement {

        public final static RootSegement instance = new RootSegement();

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            return rootObject;
        }
    }

    static class SelfSegement implements Segement {

        public final static SelfSegement instance = new SelfSegement();

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            return currentObject;
        }
    }

    static class SizeSegement implements Segement {

        public final static SizeSegement instance = new SizeSegement();

        public Integer eval(JSONPath path, Object rootObject, Object currentObject) {
            if (currentObject == null) {
                return 0;
            }

            if (currentObject instanceof Collection) {
                return ((Collection) currentObject).size();
            }

            if (currentObject instanceof Object[]) {
                return ((Object[]) currentObject).length;
            }

            if (currentObject.getClass().isArray()) {
                return Array.getLength(currentObject);
            }

            if (currentObject instanceof Map) {
                return ((Map) currentObject).size();
            }

            throw new UnsupportedOperationException();
        }
    }

    static class PropertySegement implements Segement {

        private final String propertyName;

        public PropertySegement(String propertyName){
            this.propertyName = propertyName;
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            return path.getPropertyValue(currentObject, propertyName, true);
        }
    }

    static class MultiPropertySegement implements Segement {

        private final String[] propertyNames;

        public MultiPropertySegement(String[] propertyNames){
            this.propertyNames = propertyNames;
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            List<Object> fieldValues = new ArrayList<Object>(propertyNames.length);

            for (String propertyName : propertyNames) {
                Object fieldValue = path.getPropertyValue(currentObject, propertyName, true);
                fieldValues.add(fieldValue);
            }

            return fieldValues;
        }
    }

    static class WildCardSegement implements Segement {

        public static WildCardSegement instance = new WildCardSegement();

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            return path.getPropertyValues(currentObject);
        }

    }

    static class ArrayAccessSegement implements Segement {

        private final int index;

        public ArrayAccessSegement(int index){
            this.index = index;
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            return path.getArrayItem(currentObject, index);
        }
    }

    static class MultiArrayAccessSegement implements Segement {

        private final int[] indexes;

        public MultiArrayAccessSegement(int[] indexes){
            this.indexes = indexes;
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            List<Object> items = new ArrayList<Object>(indexes.length);
            for (int i = 0; i < indexes.length; ++i) {
                Object item = path.getArrayItem(currentObject, indexes[i]);
                items.add(item);
            }
            return items;
        }
    }

    static class RangeArrayAccessSegement implements Segement {

        private final int start;
        private final int end;
        private final int step;

        public RangeArrayAccessSegement(int start, int end, int step){
            this.start = start;
            this.end = end;
            this.step = step;
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            int size = SizeSegement.instance.eval(path, rootObject, currentObject);
            List<Object> items = new ArrayList<Object>((end - start) / step + 1);
            for (int i = start; i <= end && i < size; i += step) {
                Object item = path.getArrayItem(currentObject, i);
                items.add(item);
            }
            return items;
        }
    }

    @SuppressWarnings("rawtypes")
    protected Object getArrayItem(final Object currentObject, int index) {
        if (currentObject == null) {
            return null;
        }

        if (currentObject instanceof List) {
            List list = (List) currentObject;

            if (index >= 0) {
                if (index < list.size()) {
                    return list.get(index);
                }
                return null;
            } else {
                if (Math.abs(index) <= list.size()) {
                    return list.get(list.size() + index);
                }
                return null;
            }
        }

        throw new UnsupportedOperationException();
    }

    protected List<Object> getPropertyValues(final Object currentObject) {
        final Class<?> currentClass = currentObject.getClass();

        JavaBeanSerializer beanSerializer = null;
        {
            ObjectSerializer serializer = serializeConfig.getObjectWriter(currentClass);
            if (serializer instanceof JavaBeanSerializer) {
                beanSerializer = (JavaBeanSerializer) serializer;
            } else if (serializer instanceof ASMJavaBeanSerializer) {
                beanSerializer = ((ASMJavaBeanSerializer) serializer).getJavaBeanSerializer();
            }
        }

        if (beanSerializer != null) {
            try {
                return beanSerializer.getFieldValues(currentObject);
            } catch (Exception e) {
                throw new JSONPathException("jsonpath error, path " + path, e);
            }
        }

        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("rawtypes")
    protected Object getPropertyValue(final Object currentObject, final String propertyName, boolean strictMode) {
        if (currentObject == null) {
            return null;
        }

        if (currentObject instanceof Map) {
            Map map = (Map) currentObject;
            return map.get(propertyName);
        }

        final Class<?> currentClass = currentObject.getClass();

        JavaBeanSerializer beanSerializer = null;
        {
            ObjectSerializer serializer = serializeConfig.getObjectWriter(currentClass);
            if (serializer instanceof JavaBeanSerializer) {
                beanSerializer = (JavaBeanSerializer) serializer;
            } else if (serializer instanceof ASMJavaBeanSerializer) {
                beanSerializer = ((ASMJavaBeanSerializer) serializer).getJavaBeanSerializer();
            }
        }
        if (beanSerializer != null) {
            try {
                return beanSerializer.getFieldValue(currentObject, propertyName);
            } catch (Exception e) {
                throw new JSONPathException("jsonpath error, path " + path + ", segement " + propertyName, e);
            }
        }

        if (currentObject instanceof List) {
            List list = (List) currentObject;

            List<Object> fieldValues = new ArrayList<Object>(list.size());

            for (int i = 0; i < list.size(); ++i) {
                Object obj = list.get(i);
                Object itemValue = getPropertyValue(obj, propertyName, strictMode);
                fieldValues.add(itemValue);
            }

            return fieldValues;
        }
        throw new JSONPathException("jsonpath error, path " + path + ", segement " + propertyName);
    }
}
