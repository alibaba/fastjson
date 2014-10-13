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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            if ("*".equals(path)) {
                this.pathSegments = new Segement[] { WildCardSegement.instance };
            } else {
                JSONPathParser parser = new JSONPathParser(path);
                this.pathSegments = parser.explain();
            }
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
        private int          level;

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
                    continue;
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

                if (level == 0) {
                    String propertyName = readName();

                    skipWhitespace();
                    return new PropertySegement(propertyName);
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

            boolean predicateFlag = false;

            if (ch == '?') {
                next();
                accept('(');
                if (ch == '@') {
                    next();
                    accept('.');
                }

                predicateFlag = true;
            }

            if (predicateFlag || IOUtils.firstIdentifier(ch)) {
                String propertyName = readName();

                skipWhitespace();

                if (predicateFlag && ch == ')') {
                    next();
                    accept(']');

                    return new NotNullFilterAccessSegement(propertyName);
                }

                if (ch == ']') {
                    next();
                    return new NotNullFilterAccessSegement(propertyName);
                }

                BinaryCompareOperator op = readOp();

                skipWhitespace();

                if (op == BinaryCompareOperator.IN || op == BinaryCompareOperator.NOT_IN) {
                    final boolean not = (op == BinaryCompareOperator.NOT_IN);
                    accept('(');

                    List<Object> valueList = new ArrayList<Object>();
                    {
                        Object value = readValue();
                        valueList.add(value);

                        for (;;) {
                            skipWhitespace();
                            if (ch != ',') {
                                break;
                            }
                            next();
                            skipWhitespace();

                            value = readValue();
                            valueList.add(value);
                        }

                        accept(')');
                        if (predicateFlag) {
                            accept(')');
                        }
                        accept(']');
                    }

                    boolean isInt = true;
                    boolean isString = true;
                    for (Object item : valueList) {
                        if (item == null) {
                            if (isInt) {
                                isInt = false;
                            }
                            continue;
                        }

                        Class<?> clazz = item.getClass();
                        if (isInt
                            && !(clazz == Byte.class || clazz == Short.class || clazz == Integer.class || clazz == Long.class)) {
                            isInt = false;
                        }

                        if (isString && clazz != String.class) {
                            isString = false;
                        }
                    }

                    if (isInt) {
                        long[] values = new long[valueList.size()];
                        for (int i = 0; i < values.length; ++i) {
                            values[i] = ((Number) valueList.get(i)).longValue();
                        }

                        return new IntInSegement(propertyName, values, not);
                    }

                    if (isString) {
                        String[] values = new String[valueList.size()];
                        valueList.toArray(values);

                        return new StringInSegement(propertyName, values, not);
                    }

                    throw new UnsupportedOperationException();
                }

                if (ch == '\'' || ch == '"') {
                    String strValue = readString();
                    if (predicateFlag) {
                        accept(')');
                    }
                    accept(']');

                    if (op == BinaryCompareOperator.RLIKE) {
                        return new RlikeSegement(propertyName, strValue, false);
                    }

                    if (op == BinaryCompareOperator.NOT_RLIKE) {
                        return new RlikeSegement(propertyName, strValue, true);
                    }

                    if (op == BinaryCompareOperator.LIKE || op == BinaryCompareOperator.NOT_LIKE) {
                        while (strValue.indexOf("%%") != -1) {
                            strValue = strValue.replaceAll("%%", "%");
                        }

                        final boolean not = (op == BinaryCompareOperator.NOT_LIKE);

                        int p0 = strValue.indexOf('%');
                        if (p0 == -1) {
                            if (op == BinaryCompareOperator.LIKE) {
                                op = BinaryCompareOperator.EQ;
                            } else {
                                op = BinaryCompareOperator.NE;
                            }
                        } else {
                            String[] items = strValue.split("%");

                            String startsWithValue = null;
                            String endsWithValue = null;
                            String[] containsValues = null;
                            if (p0 == 0) {
                                if (strValue.charAt(strValue.length() - 1) == '%') {
                                    containsValues = new String[items.length - 1];
                                    System.arraycopy(items, 1, containsValues, 0, containsValues.length);
                                } else {
                                    endsWithValue = items[items.length - 1];
                                    if (items.length > 2) {
                                        containsValues = new String[items.length - 2];
                                        System.arraycopy(items, 1, containsValues, 0, containsValues.length);
                                    }
                                }
                            } else if (strValue.charAt(strValue.length() - 1) == '%') {
                                containsValues = items;
                            } else {
                                if (items.length == 1) {
                                    startsWithValue = items[0];
                                } else if (items.length == 2) {
                                    startsWithValue = items[0];
                                    endsWithValue = items[1];
                                } else {
                                    startsWithValue = items[0];
                                    endsWithValue = items[items.length - 1];
                                    containsValues = new String[items.length - 2];
                                    System.arraycopy(items, 1, containsValues, 0, containsValues.length);
                                }
                            }

                            return new MatchSegement(propertyName, startsWithValue, endsWithValue, containsValues, not);

                        }
                    }

                    return new StringCompareFilterAccessSegement(propertyName, strValue, op);
                }

                if (isDigitFirst(ch)) {
                    long value = readLongValue();

                    if (predicateFlag) {
                        accept(')');
                    }
                    accept(']');

                    return new IntCompareFilterAccessSegement(propertyName, value, op);
                }

                if (ch == 'n') {
                    String name = readName();
                    if ("null".equals(name)) {
                        if (predicateFlag) {
                            accept(')');
                        }
                        accept(']');

                        if (op == BinaryCompareOperator.EQ) {
                            return new NullFilterAccessSegement(propertyName);
                        }

                        if (op == BinaryCompareOperator.NE) {
                            return new NotNullFilterAccessSegement(propertyName);
                        }

                        throw new UnsupportedOperationException();
                    }
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

        protected long readLongValue() {
            int beginIndex = pos - 1;
            if (ch == '+' || ch == '-') {
                next();
            }

            while (ch >= '0' && ch <= '9') {
                next();
            }

            int endIndex = pos - 1;
            String text = path.substring(beginIndex, endIndex);
            long value = Long.parseLong(text);
            return value;
        }

        protected Object readValue() {
            if (isDigitFirst(ch)) {
                return readLongValue();
            }

            if (ch == '"' || ch == '\'') {
                return readString();
            }

            if (ch == 'n') {
                String name = readName();

                if ("null".equals(name)) {
                    return null;
                } else {
                    throw new JSONPathException(path);
                }
            }

            throw new UnsupportedOperationException();
        }

        static boolean isDigitFirst(char ch) {
            return ch == '-' || ch == '+' || (ch >= '0' && ch <= '9');
        }

        protected BinaryCompareOperator readOp() {
            BinaryCompareOperator op = null;
            if (ch == '=') {
                next();
                op = BinaryCompareOperator.EQ;
            } else if (ch == '!') {
                next();
                accept('=');
                op = BinaryCompareOperator.NE;
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
            }

            if (op == null) {
                String name = readName();

                if ("not".equalsIgnoreCase(name)) {
                    skipWhitespace();

                    name = readName();

                    if ("like".equalsIgnoreCase(name)) {
                        op = BinaryCompareOperator.NOT_LIKE;
                    } else if ("rlike".equalsIgnoreCase(name)) {
                        op = BinaryCompareOperator.NOT_RLIKE;
                    } else if ("in".equalsIgnoreCase(name)) {
                        op = BinaryCompareOperator.NOT_IN;
                    } else {
                        throw new UnsupportedOperationException();
                    }
                } else {
                    if ("like".equalsIgnoreCase(name)) {
                        op = BinaryCompareOperator.LIKE;
                    } else if ("rlike".equalsIgnoreCase(name)) {
                        op = BinaryCompareOperator.RLIKE;
                    } else if ("in".equalsIgnoreCase(name)) {
                        op = BinaryCompareOperator.IN;
                    } else {
                        throw new UnsupportedOperationException();
                    }
                }
            }
            return op;
        }

        String readName() {
            if (!IOUtils.firstIdentifier(ch)) {
                throw new JSONPathException("illeal jsonpath syntax. " + path);
            }

            int beginIndex = pos - 1;
            while (!isEOF()) {
                boolean identifierFlag = IOUtils.isIdent(ch);
                if (!identifierFlag) {
                    break;
                }
                next();
            }

            int endIndex = pos - 1;
            if (isEOF() && IOUtils.isIdent(ch)) {
                endIndex = pos;
            }

            String propertyName = path.substring(beginIndex, endIndex);

            return propertyName;
        }

        String readString() {
            char quoate = ch;
            next();

            int beginIndex = pos - 1;
            while (ch != quoate && !isEOF()) {
                next();
            }

            String strValue = path.substring(beginIndex, isEOF() ? pos : pos - 1);

            accept(quoate);

            return strValue;
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

            for (;;) {
                Segement segment = readSegement();
                if (segment == null) {
                    break;
                }
                segements[level++] = segment;
            }

            if (level == segements.length) {
                return segements;
            }

            Segement[] result = new Segement[level];
            System.arraycopy(segements, 0, result, 0, level);
            return result;
        }

        Segement buildArraySegement(String indexText) {
            final int indexTextLen = indexText.length();
            final char firstChar = indexText.charAt(0);
            final char lastChar = indexText.charAt(indexTextLen - 1);

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

    // static class RootSegement implements Segement {
    //
    // public final static RootSegement instance = new RootSegement();
    //
    // public Object eval(JSONPath path, Object rootObject, Object currentObject) {
    // return rootObject;
    // }
    // }

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

    static class NullFilterAccessSegement extends FilterAccessSegement {

        private final String propertyName;

        public NullFilterAccessSegement(String propertyName){
            this.propertyName = propertyName;
        }

        @Override
        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, false);

            return propertyValue == null;
        }
    }

    static class IntInSegement extends FilterAccessSegement {

        private final String propertyName;
        private final long[] values;
        private final boolean not;

        public IntInSegement(String propertyName, long[] values, boolean not){
            this.propertyName = propertyName;
            this.values = values;
            this.not = not;
        }

        @Override
        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, false);

            if (propertyValue == null) {
                return false;
            }

            if (propertyValue instanceof Number) {
                long longPropertyValue = ((Number) propertyValue).longValue();
                for (long value : values) {
                    if (value == longPropertyValue) {
                        return !not;
                    }
                }
            }

            return not;
        }
    }

    static class StringInSegement extends FilterAccessSegement {

        private final String   propertyName;
        private final String[] values;
        private final boolean not;

        public StringInSegement(String propertyName, String[] values, boolean not){
            this.propertyName = propertyName;
            this.values = values;
            this.not = not;
        }

        @Override
        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, false);

            for (String value : values) {
                if (value == propertyValue) {
                    return !not;
                } else if (value != null && value.equals(propertyValue)) {
                    return !not;
                }
            }

            return not;
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

    static class MatchSegement extends FilterAccessSegement {

        private final String   propertyName;
        private final String   startsWithValue;
        private final String   endsWithValue;
        private final String[] containsValues;
        private final int      minLength;
        private final boolean  not;

        public MatchSegement(String propertyName, String startsWithValue, String endsWithValue,
                             String[] containsValues, boolean not){
            this.propertyName = propertyName;
            this.startsWithValue = startsWithValue;
            this.endsWithValue = endsWithValue;
            this.containsValues = containsValues;
            this.not = not;

            int len = 0;
            if (startsWithValue != null) {
                len += startsWithValue.length();
            }

            if (endsWithValue != null) {
                len += endsWithValue.length();
            }

            if (containsValues != null) {
                for (String item : containsValues) {
                    len += item.length();
                }
            }

            this.minLength = len;
        }

        @Override
        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, false);

            if (propertyValue == null) {
                return false;
            }

            final String strPropertyValue = propertyValue.toString();

            if (strPropertyValue.length() < minLength) {
                return not;
            }

            int start = 0;
            if (startsWithValue != null) {
                if (!strPropertyValue.startsWith(startsWithValue)) {
                    return not;
                }
                start += startsWithValue.length();
            }

            if (containsValues != null) {
                for (String containsValue : containsValues) {
                    int index = strPropertyValue.indexOf(containsValue, start);
                    if (index == -1) {
                        return not;
                    }
                    start = index + containsValue.length();
                }
            }

            if (endsWithValue != null) {
                if (!strPropertyValue.endsWith(endsWithValue)) {
                    return not;
                }
            }

            return !not;
        }
    }

    static class RlikeSegement extends FilterAccessSegement {

        private final String  propertyName;
        private final Pattern pattern;
        private final boolean not;

        public RlikeSegement(String propertyName, String pattern, boolean not){
            this.propertyName = propertyName;
            this.pattern = Pattern.compile(pattern);
            this.not = not;
        }

        @Override
        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, false);

            if (propertyValue == null) {
                return false;
            }

            String strPropertyValue = propertyValue.toString();
            Matcher m = pattern.matcher(strPropertyValue);
            boolean match = m.matches();

            if (not) {
                match = !match;
            }

            return match;
        }
    }

    static class StringCompareFilterAccessSegement extends FilterAccessSegement {

        private final String                propertyName;
        private final String                value;
        private final BinaryCompareOperator op;

        public StringCompareFilterAccessSegement(String propertyName, String value, BinaryCompareOperator op){
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

            if (op == BinaryCompareOperator.EQ) {
                return value.equals(propertyValue);
            } else if (op == BinaryCompareOperator.NE) {
                return !value.equals(propertyValue);
            }

            int compareResult = value.compareTo(propertyValue.toString());
            if (op == BinaryCompareOperator.GE) {
                return compareResult <= 0;
            } else if (op == BinaryCompareOperator.GT) {
                return compareResult < 0;
            } else if (op == BinaryCompareOperator.LE) {
                return compareResult >= 0;
            } else if (op == BinaryCompareOperator.LT) {
                return compareResult > 0;
            }

            return false;
        }
    }

    static enum BinaryCompareOperator {
        EQ, NE, GT, GE, LT, LE, LIKE, NOT_LIKE, RLIKE, NOT_RLIKE, IN, NOT_IN
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Collection<Object> getPropertyValues(final Object currentObject) {
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

        if (currentObject instanceof Map) {
            Map map = (Map) currentObject;
            return map.values();
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
