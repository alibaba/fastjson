package com.alibaba.fastjson;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.alibaba.fastjson.serializer.ASMJavaBeanSerializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.IOUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 * @since 1.2.0
 */
public class JSONPath implements ObjectSerializer {

    private static int                             CACHE_SIZE = 1024;
    private static ConcurrentMap<String, JSONPath> pathCache  = new ConcurrentHashMap<String, JSONPath>(128, 0.75f, 1);

    private final String                           path;
    private Segement[]                             pathSegments;

    private SerializeConfig                        serializeConfig;

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

        if (pathSegments == null) {
            JSONPathParser parser = new JSONPathParser(path);
            this.pathSegments = parser.explain();
        }

        Object currentObject = rootObject;
        for (int i = 0; i < pathSegments.length; ++i) {
            currentObject = pathSegments[i].eval(this, rootObject, currentObject);
        }
        return currentObject;
    }

    public static Object eval(Object rootObject, String path) {
        JSONPath jsonpath = pathCache.get(path);
        if (jsonpath == null) {
            jsonpath = new JSONPath(path);
            if (pathCache.size() < CACHE_SIZE) {
                pathCache.putIfAbsent(path, jsonpath);
                jsonpath = pathCache.get(path);
            }
        }
        return jsonpath.eval(rootObject);
    }

    public String getPath() {
        return path;
    }

    static class JSONPathParser {

        private final String path;
        private int          pos;
        private char         ch;

        public JSONPathParser(String path){
            this.path = path;
            next();
        }

        void next() {
            ch = path.charAt(pos++);
        }

        boolean isEOF() {
            return pos >= path.length();
        }

        Segement readSegement() {
            while (!isEOF()) {
                skipWhitespace();

                if (ch == '@') {
                    next();
                    return SelfSegement.instance;
                }

                if (ch == '$') {
                    next();
                    return RootSegement.instance;
                }

                if (ch == '.') {
                    next();
                    if (ch == '*') {
                        if (!isEOF()) {
                            next();
                        }

                        return WildCardSegement.instance;
                    }

                    String propertyName = readName();
                    if (ch == '(') {
                        next();
                        if (ch == ')') {
                            if (!isEOF()) {
                                next();
                            }

                            if ("size".equals(propertyName)) {
                                return SizeSegement.instance;
                            }

                            throw new UnsupportedOperationException();
                        }

                        throw new UnsupportedOperationException();
                    }

                    return new PropertySegement(propertyName);
                }

                if (ch == '[') {
                    return parseArrayAccess();
                }

                throw new UnsupportedOperationException();
            }

            return null;
        }

        public final void skipWhitespace() {
            for (;;) {
                if (ch < IOUtils.whitespaceFlags.length && IOUtils.whitespaceFlags[ch]) {
                    next();
                    continue;
                } else {
                    break;
                }
            }
        }

        Segement parseArrayAccess() {
            accept('[');

            if (ch == '?') {
                next();
                accept('(');
                accept('@');
                accept('.');

                String propertyName = readName();

                skipWhitespace();

                if (ch == ')') {
                    next();
                    accept(']');

                    return new NotNullFilterAccessSegement(propertyName);
                }

                BinaryCompareOperator op;
                if (ch == '=') {
                    next();
                    op = BinaryCompareOperator.EQ;
                } else if (ch == '!') {
                    next();
                    accept('=');
                    op = BinaryCompareOperator.EQ;
                } else if (ch == '<') {
                    next();
                    if (ch == '=') {
                        next();
                        op = BinaryCompareOperator.LE;
                    } else {
                        op = BinaryCompareOperator.LT;
                    }
                } else if (ch == '>') {
                    next();
                    if (ch == '=') {
                        next();
                        op = BinaryCompareOperator.GE;
                    } else {
                        op = BinaryCompareOperator.GT;
                    }
                } else {
                    throw new UnsupportedOperationException();
                }

                skipWhitespace();

                if (ch == '-' || ch == '+' || (ch >= '0' && ch <= '9')) {
                    int beginIndex = pos - 1;
                    boolean negative = false;
                    if (ch == '+') {
                        next();
                    } else if (ch == '-') {
                        negative = true;
                    }

                    while (ch >= '0' && ch <= '9') {
                        next();
                    }

                    String text = path.substring(beginIndex, isEOF() ? pos : pos - 1);
                    long value = Long.parseLong(text);

                    next();
                    accept(']');

                    return new IntCompareFilterAccessSegement(propertyName, value, op);
                }

                throw new UnsupportedOperationException();
                // accept(')');
            }

            int start = pos - 1;
            while (ch != ']' && !isEOF()) {
                next();
            }

            String text = path.substring(start, pos - 1);

            if (!isEOF()) {
                accept(']');
            }

            return buildArraySegement(text);
        }

        String readName() {
            final boolean firstFlag = ch >= IOUtils.firstIdentifierFlags.length || IOUtils.firstIdentifierFlags[ch];

            if (!firstFlag) {
                throw new JSONPathException("illeal jsonpath syntax. " + path);
            }

            int beginIndex = pos - 1;
            while (!isEOF()) {
                boolean identifierFlag = ch >= IOUtils.identifierFlags.length || IOUtils.identifierFlags[ch];
                if (!identifierFlag) {
                    break;
                }
                next();
            }

            String propertyName = path.substring(beginIndex, isEOF() ? pos : pos - 1);

            return propertyName;
        }

        void accept(char expect) {
            if (ch != expect) {
                throw new JSONPathException("expect '" + expect + ", but '" + ch + "'");
            }

            if (!isEOF()) {
                next();
            }
        }

        public Segement[] explain() {
            if (path == null || path.isEmpty()) {
                throw new IllegalArgumentException();
            }

            Segement[] segements = new Segement[8];
            int len = 0;

            for (;;) {
                Segement segment = readSegement();
                if (segment == null) {
                    break;
                }
                segements[len++] = segment;
            }

            if (len == segements.length) {
                return segements;
            }

            Segement[] result = new Segement[len];
            System.arraycopy(segements, 0, result, 0, len);
            return result;
        }

        Segement buildArraySegement(String indexText) {
            final int indexTextLen = indexText.length();
            final char firstChar = indexText.charAt(0);
            final char lastChar = indexText.charAt(indexTextLen - 1);

            if (firstChar == '?') {
                if (indexTextLen < 3 || lastChar != ')' || indexText.charAt(1) != '(') {
                    throw new UnsupportedOperationException();
                }

                throw new UnsupportedOperationException();
            }

            int commaIndex = indexText.indexOf(',');

            if (indexText.length() > 2 && firstChar == '\'' && lastChar == '\'') {

                if (commaIndex == -1) {
                    String propertyName = indexText.substring(1, indexTextLen - 1);
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
                    String str = indexesText[i];
                    if (str.isEmpty()) {
                        if (i == 0) {
                            indexes[i] = 0;
                        } else {
                            throw new UnsupportedOperationException();
                        }
                    } else {
                        indexes[i] = Integer.parseInt(str);
                    }
                }

                int start = indexes[0];
                int end;
                if (indexes.length > 1) {
                    end = indexes[1];
                } else {
                    end = -1;
                }
                int step;
                if (indexes.length == 3) {
                    step = indexes[2];
                } else {
                    step = 1;
                }

                if (end >= 0 && end < start) {
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

        @SuppressWarnings("rawtypes")
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
            int start = this.start >= 0 ? this.start : this.start + size;
            int end = this.end >= 0 ? this.end : this.end + size;

            List<Object> items = new ArrayList<Object>((end - start) / step + 1);
            for (int i = start; i <= end && i < size; i += step) {
                Object item = path.getArrayItem(currentObject, i);
                items.add(item);
            }
            return items;
        }
    }

    static class NotNullFilterAccessSegement extends FilterAccessSegement {

        private final String propertyName;

        public NotNullFilterAccessSegement(String propertyName){
            this.propertyName = propertyName;
        }

        @Override
        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, false);

            return propertyValue != null;
        }
    }

    static class IntCompareFilterAccessSegement extends FilterAccessSegement {

        private final String                propertyName;
        private final long                  value;
        private final BinaryCompareOperator op;

        public IntCompareFilterAccessSegement(String propertyName, long value, BinaryCompareOperator op){
            this.propertyName = propertyName;
            this.value = value;
            this.op = op;
        }

        @Override
        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, false);

            if (propertyValue == null) {
                return false;
            }

            if (!(propertyValue instanceof Number)) {
                return false;
            }

            long longValue = ((Number) propertyValue).longValue();

            if (op == BinaryCompareOperator.EQ) {
                return longValue == value;
            } else if (op == BinaryCompareOperator.NE) {
                return longValue != value;
            } else if (op == BinaryCompareOperator.GE) {
                return longValue >= value;
            } else if (op == BinaryCompareOperator.GT) {
                return longValue > value;
            } else if (op == BinaryCompareOperator.LE) {
                return longValue <= value;
            } else if (op == BinaryCompareOperator.LT) {
                return longValue < value;
            }

            return false;
        }
    }

    static enum BinaryCompareOperator {
        EQ, NE, GT, GE, LT, LE
    }

    static abstract class FilterAccessSegement implements Segement {

        @SuppressWarnings("rawtypes")
        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            if (currentObject == null) {
                return null;
            }

            List<Object> items = new ArrayList<Object>();

            if (currentObject instanceof Iterable) {
                Iterator it = ((Iterable) currentObject).iterator();
                while (it.hasNext()) {
                    Object item = it.next();

                    if (apply(path, rootObject, currentObject, item)) {
                        items.add(item);
                    }
                }

                return items;
            }

            throw new UnsupportedOperationException();
        }

        public abstract boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item);
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

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
                                                                                                               throws IOException {
        serializer.write(path);
    }
}
