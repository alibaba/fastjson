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

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
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

    private final String path;
    private Segement[]   segments;

    private SerializeConfig serializeConfig;
    private ParserConfig    parserConfig;

    public JSONPath(String path){
        this(path, SerializeConfig.getGlobalInstance(), ParserConfig.getGlobalInstance());
    }

    public JSONPath(String path, SerializeConfig serializeConfig, ParserConfig parserConfig){
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.path = path;
        this.serializeConfig = serializeConfig;
        this.parserConfig = parserConfig;
    }

    protected void init() {
        if (segments != null) {
            return;
        }

        if ("*".equals(path)) {
            this.segments = new Segement[] { WildCardSegement.instance };
        } else {
            JSONPathParser parser = new JSONPathParser(path);
            this.segments = parser.explain();
        }
    }

    public Object eval(Object rootObject) {
        if (rootObject == null) {
            return null;
        }

        init();

        Object currentObject = rootObject;
        for (int i = 0; i < segments.length; ++i) {
            currentObject = segments[i].eval(this, rootObject, currentObject);
        }
        return currentObject;
    }

    public boolean contains(Object rootObject) {
        if (rootObject == null) {
            return false;
        }

        init();

        Object currentObject = rootObject;
        for (int i = 0; i < segments.length; ++i) {
            currentObject = segments[i].eval(this, rootObject, currentObject);
            if (currentObject == null) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("rawtypes")
    public boolean containsValue(Object rootObject, Object value) {
        Object currentObject = eval(rootObject);

        if (currentObject == value) {
            return true;
        }

        if (currentObject == null) {
            return false;
        }

        if (currentObject instanceof Iterable) {
            Iterator it = ((Iterable) currentObject).iterator();
            while (it.hasNext()) {
                Object item = it.next();
                if (eq(item, value)) {
                    return true;
                }
            }

            return false;
        }

        return eq(currentObject, value);
    }

    public int size(Object rootObject) {
        if (rootObject == null) {
            return -1;
        }

        init();

        Object currentObject = rootObject;
        for (int i = 0; i < segments.length; ++i) {
            currentObject = segments[i].eval(this, rootObject, currentObject);
        }

        return evalSize(currentObject);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void arrayAdd(Object rootObject, Object... values) {
        if (values == null || values.length == 0) {
            return;
        }

        if (rootObject == null) {
            return;
        }

        init();

        Object currentObject = rootObject;
        Object parentObject = null;
        for (int i = 0; i < segments.length; ++i) {
            if (i == segments.length - 1) {
                parentObject = currentObject;
            }
            currentObject = segments[i].eval(this, rootObject, currentObject);
        }

        Object result = currentObject;

        if (result == null) {
            throw new JSONPathException("value not found in path " + path);
        }

        if (result instanceof Collection) {
            Collection collection = (Collection) result;
            for (Object value : values) {
                collection.add(value);
            }
            return;
        }

        Class<?> resultClass = result.getClass();

        Object newResult;
        if (resultClass.isArray()) {
            int length = Array.getLength(result);
            Object descArray = Array.newInstance(resultClass.getComponentType(), length + values.length);

            System.arraycopy(result, 0, descArray, 0, length);
            for (int i = 0; i < values.length; ++i) {
                Array.set(descArray, length + i, values[i]);

            }
            newResult = descArray;
        } else {
            throw new UnsupportedOperationException();
        }

        Segement lastSegement = segments[segments.length - 1];
        if (lastSegement instanceof PropertySegement) {
            PropertySegement propertySegement = (PropertySegement) lastSegement;
            propertySegement.setValue(this, parentObject, newResult);
            return;
        }

        if (lastSegement instanceof ArrayAccessSegement) {
            ((ArrayAccessSegement) lastSegement).setValue(this, parentObject, newResult);
            return;
        }

        throw new UnsupportedOperationException();
    }

    public boolean set(Object rootObject, Object value) {
        if (rootObject == null) {
            return false;
        }

        init();

        Object currentObject = rootObject;
        Object parentObject = null;
        for (int i = 0; i < segments.length; ++i) {
            if (i == segments.length - 1) {
                parentObject = currentObject;
                break;
            }
            currentObject = segments[i].eval(this, rootObject, currentObject);
            if (currentObject == null) {
                break;
            }
        }

        if (parentObject == null) {
            return false;
        }

        Segement lastSegement = segments[segments.length - 1];
        if (lastSegement instanceof PropertySegement) {
            PropertySegement propertySegement = (PropertySegement) lastSegement;
            propertySegement.setValue(this, parentObject, value);
            return true;
        }

        if (lastSegement instanceof ArrayAccessSegement) {
            return ((ArrayAccessSegement) lastSegement).setValue(this, parentObject, value);
        }

        throw new UnsupportedOperationException();
    }

    public static Object eval(Object rootObject, String path) {
        JSONPath jsonpath = compile(path);
        return jsonpath.eval(rootObject);
    }

    public static int size(Object rootObject, String path) {
        JSONPath jsonpath = compile(path);
        Object result = jsonpath.eval(rootObject);
        return jsonpath.evalSize(result);
    }

    public static boolean contains(Object rootObject, String path) {
        if (rootObject == null) {
            return false;
        }

        JSONPath jsonpath = compile(path);
        return jsonpath.contains(rootObject);
    }

    public static boolean containsValue(Object rootObject, String path, Object value) {
        JSONPath jsonpath = compile(path);
        return jsonpath.containsValue(rootObject, value);
    }

    public static void arrayAdd(Object rootObject, String path, Object... values) {
        JSONPath jsonpath = compile(path);
        jsonpath.arrayAdd(rootObject, values);
    }

    public static void set(Object rootObject, String path, Object value) {
        JSONPath jsonpath = compile(path);
        jsonpath.set(rootObject, value);
    }

    public static JSONPath compile(String path) {
        JSONPath jsonpath = pathCache.get(path);
        if (jsonpath == null) {
            jsonpath = new JSONPath(path);
            if (pathCache.size() < CACHE_SIZE) {
                pathCache.putIfAbsent(path, jsonpath);
                jsonpath = pathCache.get(path);
            }
        }
        return jsonpath;
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
            if (level == 0 && path.length() == 1) {
                if (isDigitFirst(ch)) {
                    int index = ch - '0';
                    return new ArrayAccessSegement(index);
                } else if ((ch >= 'a' && ch <= 'z') || ((ch >= 'A' && ch <= 'Z'))) {
                    return new PropertySegement(Character.toString(ch));
                }
            }
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

                if (ch == '.' || ch == '/') {
                    next();
                    if (ch == '*') {
                        if (!isEOF()) {
                            next();
                        }

                        return WildCardSegement.instance;
                    }
                    
                    if (isDigitFirst(ch)) {
                        return parseArrayAccess(false);
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
                    return parseArrayAccess(true);
                }

                if (level == 0) {
                    String propertyName = readName();

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

        Segement parseArrayAccess(boolean acceptBracket) {
            if (acceptBracket) {
                accept('[');
            }

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
                    if (acceptBracket) {
                        accept(']');
                    }

                    return new FilterSegement(new NotNullSegement(propertyName));
                }

                if (acceptBracket && ch == ']') {
                    next();
                    return new FilterSegement(new NotNullSegement(propertyName));
                }

                Operator op = readOp();

                skipWhitespace();

                if (op == Operator.BETWEEN || op == Operator.NOT_BETWEEN) {
                    final boolean not = (op == Operator.NOT_BETWEEN);

                    Object startValue = readValue();

                    String name = readName();

                    if (!"and".equalsIgnoreCase(name)) {
                        throw new JSONPathException(path);
                    }

                    Object endValue = readValue();

                    if (startValue == null || endValue == null) {
                        throw new JSONPathException(path);
                    }

                    if (isInt(startValue.getClass()) && isInt(endValue.getClass())) {
                        Filter filter = new IntBetweenSegement(propertyName, ((Number) startValue).longValue(),
                                                               ((Number) endValue).longValue(), not);
                        return new FilterSegement(filter);
                    }

                    throw new JSONPathException(path);
                }

                if (op == Operator.IN || op == Operator.NOT_IN) {
                    final boolean not = (op == Operator.NOT_IN);
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

                            value = readValue();
                            valueList.add(value);
                        }

                        accept(')');
                        if (predicateFlag) {
                            accept(')');
                        }

                        if (acceptBracket) {
                            accept(']');
                        }
                    }

                    boolean isInt = true;
                    boolean isIntObj = true;
                    boolean isString = true;
                    for (Object item : valueList) {
                        if (item == null) {
                            if (isInt) {
                                isInt = false;
                            }
                            continue;
                        }

                        Class<?> clazz = item.getClass();
                        if (isInt && !(clazz == Byte.class || clazz == Short.class || clazz == Integer.class
                                       || clazz == Long.class)) {
                            isInt = false;
                            isIntObj = false;
                        }

                        if (isString && clazz != String.class) {
                            isString = false;
                        }
                    }

                    if (valueList.size() == 1 && valueList.get(0) == null) {
                        if (not) {
                            return new FilterSegement(new NotNullSegement(propertyName));
                        } else {
                            return new FilterSegement(new NullSegement(propertyName));
                        }
                    }

                    if (isInt) {
                        if (valueList.size() == 1) {
                            long value = ((Number) valueList.get(0)).longValue();
                            Operator intOp = not ? Operator.NE : Operator.EQ;
                            return new FilterSegement(new IntOpSegement(propertyName, value, intOp));
                        }

                        long[] values = new long[valueList.size()];
                        for (int i = 0; i < values.length; ++i) {
                            values[i] = ((Number) valueList.get(i)).longValue();
                        }

                        return new FilterSegement(new IntInSegement(propertyName, values, not));
                    }

                    if (isString) {
                        if (valueList.size() == 1) {
                            String value = (String) valueList.get(0);

                            Operator intOp = not ? Operator.NE : Operator.EQ;
                            return new FilterSegement(new StringOpSegement(propertyName, value, intOp));
                        }

                        String[] values = new String[valueList.size()];
                        valueList.toArray(values);

                        return new FilterSegement(new StringInSegement(propertyName, values, not));
                    }

                    if (isIntObj) {
                        Long[] values = new Long[valueList.size()];
                        for (int i = 0; i < values.length; ++i) {
                            Number item = (Number) valueList.get(i);
                            if (item != null) {
                                values[i] = item.longValue();
                            }
                        }

                        return new FilterSegement(new IntObjInSegement(propertyName, values, not));
                    }

                    throw new UnsupportedOperationException();
                }

                if (ch == '\'' || ch == '"') {
                    String strValue = readString();
                    if (predicateFlag) {
                        accept(')');
                    }
                    
                    if (acceptBracket) {
                        accept(']');
                    }

                    if (op == Operator.RLIKE) {
                        return new FilterSegement(new RlikeSegement(propertyName, strValue, false));
                    }

                    if (op == Operator.NOT_RLIKE) {
                        return new FilterSegement(new RlikeSegement(propertyName, strValue, true));
                    }

                    if (op == Operator.LIKE || op == Operator.NOT_LIKE) {
                        while (strValue.indexOf("%%") != -1) {
                            strValue = strValue.replaceAll("%%", "%");
                        }

                        final boolean not = (op == Operator.NOT_LIKE);

                        int p0 = strValue.indexOf('%');
                        if (p0 == -1) {
                            if (op == Operator.LIKE) {
                                op = Operator.EQ;
                            } else {
                                op = Operator.NE;
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

                            return new FilterSegement(new MatchSegement(propertyName, startsWithValue, endsWithValue,
                                                                        containsValues, not));

                        }
                    }

                    return new FilterSegement(new StringOpSegement(propertyName, strValue, op));
                }

                if (isDigitFirst(ch)) {
                    long value = readLongValue();

                    if (predicateFlag) {
                        accept(')');
                    }
                    
                    if (acceptBracket) {
                        accept(']');
                    }

                    return new FilterSegement(new IntOpSegement(propertyName, value, op));
                }

                if (ch == 'n') {
                    String name = readName();
                    if ("null".equals(name)) {
                        if (predicateFlag) {
                            accept(')');
                        }
                        accept(']');

                        if (op == Operator.EQ) {
                            return new FilterSegement(new NullSegement(propertyName));
                        }

                        if (op == Operator.NE) {
                            return new FilterSegement(new NotNullSegement(propertyName));
                        }

                        throw new UnsupportedOperationException();
                    }
                }

                throw new UnsupportedOperationException();
                // accept(')');
            }

            int start = pos - 1;
            while (ch != ']' && ch != '/' && !isEOF()) {
                next();
            }
            
            int end;
            if (acceptBracket) {
                end = pos - 1;
            } else {
                if (ch == '/') {
                    end = pos - 1;
                } else {
                    end = pos;
                }
            }
            
            String text = path.substring(start, end);

            Segement segment = buildArraySegement(text);

            if (acceptBracket && !isEOF()) {
                accept(']');
            }

            return segment;
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
            skipWhitespace();

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

        protected Operator readOp() {
            Operator op = null;
            if (ch == '=') {
                next();
                op = Operator.EQ;
            } else if (ch == '!') {
                next();
                accept('=');
                op = Operator.NE;
            } else if (ch == '<') {
                next();
                if (ch == '=') {
                    next();
                    op = Operator.LE;
                } else {
                    op = Operator.LT;
                }
            } else if (ch == '>') {
                next();
                if (ch == '=') {
                    next();
                    op = Operator.GE;
                } else {
                    op = Operator.GT;
                }
            }

            if (op == null) {
                String name = readName();

                if ("not".equalsIgnoreCase(name)) {
                    skipWhitespace();

                    name = readName();

                    if ("like".equalsIgnoreCase(name)) {
                        op = Operator.NOT_LIKE;
                    } else if ("rlike".equalsIgnoreCase(name)) {
                        op = Operator.NOT_RLIKE;
                    } else if ("in".equalsIgnoreCase(name)) {
                        op = Operator.NOT_IN;
                    } else if ("between".equalsIgnoreCase(name)) {
                        op = Operator.NOT_BETWEEN;
                    } else {
                        throw new UnsupportedOperationException();
                    }
                } else {
                    if ("like".equalsIgnoreCase(name)) {
                        op = Operator.LIKE;
                    } else if ("rlike".equalsIgnoreCase(name)) {
                        op = Operator.RLIKE;
                    } else if ("in".equalsIgnoreCase(name)) {
                        op = Operator.IN;
                    } else if ("between".equalsIgnoreCase(name)) {
                        op = Operator.BETWEEN;
                    } else {
                        throw new UnsupportedOperationException();
                    }
                }
            }
            return op;
        }

        String readName() {
            skipWhitespace();

            if (!IOUtils.firstIdentifier(ch)) {
                throw new JSONPathException("illeal jsonpath syntax. " + path);
            }

            StringBuffer buf = new StringBuffer();
            while (!isEOF()) {
                if (ch == '\\') {
                    next();
                    buf.append(ch);
                    next();
                    continue;
                }

                boolean identifierFlag = IOUtils.isIdent(ch);
                if (!identifierFlag) {
                    break;
                }
                buf.append(ch);
                next();
            }

            if (isEOF() && IOUtils.isIdent(ch)) {
                buf.append(ch);
            }

            String propertyName = buf.toString();

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
                return new MultiIndexSegement(indexes);
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
                return new RangeSegement(start, end, step);
            }

            throw new UnsupportedOperationException();
        }
    }

    interface Segement {

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

        public Integer eval(JSONPath path, Object rootObject, Object currentObject) {
            return path.evalSize(currentObject);
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

        public void setValue(JSONPath path, Object parent, Object value) {
            path.setPropertyValue(parent, propertyName, value);
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

        public boolean setValue(JSONPath path, Object currentObject, Object value) {
            return path.setArrayItem(path, currentObject, index, value);
        }
    }

    static class MultiIndexSegement implements Segement {

        private final int[] indexes;

        public MultiIndexSegement(int[] indexes){
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

    static class RangeSegement implements Segement {

        private final int start;
        private final int end;
        private final int step;

        public RangeSegement(int start, int end, int step){
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

    static class NotNullSegement implements Filter {

        private final String propertyName;

        public NotNullSegement(String propertyName){
            this.propertyName = propertyName;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, false);

            return propertyValue != null;
        }
    }

    static class NullSegement implements Filter {

        private final String propertyName;

        public NullSegement(String propertyName){
            this.propertyName = propertyName;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, false);

            return propertyValue == null;
        }
    }

    static class IntInSegement implements Filter {

        private final String  propertyName;
        private final long[]  values;
        private final boolean not;

        public IntInSegement(String propertyName, long[] values, boolean not){
            this.propertyName = propertyName;
            this.values = values;
            this.not = not;
        }

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

    static class IntBetweenSegement implements Filter {

        private final String  propertyName;
        private final long    startValue;
        private final long    endValue;
        private final boolean not;

        public IntBetweenSegement(String propertyName, long startValue, long endValue, boolean not){
            this.propertyName = propertyName;
            this.startValue = startValue;
            this.endValue = endValue;
            this.not = not;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, false);

            if (propertyValue == null) {
                return false;
            }

            if (propertyValue instanceof Number) {
                long longPropertyValue = ((Number) propertyValue).longValue();
                if (longPropertyValue >= startValue && longPropertyValue <= endValue) {
                    return !not;
                }
            }

            return not;
        }
    }

    static class IntObjInSegement implements Filter {

        private final String  propertyName;
        private final Long[]  values;
        private final boolean not;

        public IntObjInSegement(String propertyName, Long[] values, boolean not){
            this.propertyName = propertyName;
            this.values = values;
            this.not = not;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, false);

            if (propertyValue == null) {
                for (Long value : values) {
                    if (value == null) {
                        return !not;
                    }
                }

                return not;
            }

            if (propertyValue instanceof Number) {
                long longPropertyValue = ((Number) propertyValue).longValue();
                for (Long value : values) {
                    if (value == null) {
                        continue;
                    }

                    if (value.longValue() == longPropertyValue) {
                        return !not;
                    }
                }
            }

            return not;
        }
    }

    static class StringInSegement implements Filter {

        private final String   propertyName;
        private final String[] values;
        private final boolean  not;

        public StringInSegement(String propertyName, String[] values, boolean not){
            this.propertyName = propertyName;
            this.values = values;
            this.not = not;
        }

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

    static class IntOpSegement implements Filter {

        private final String   propertyName;
        private final long     value;
        private final Operator op;

        public IntOpSegement(String propertyName, long value, Operator op){
            this.propertyName = propertyName;
            this.value = value;
            this.op = op;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, false);

            if (propertyValue == null) {
                return false;
            }

            if (!(propertyValue instanceof Number)) {
                return false;
            }

            long longValue = ((Number) propertyValue).longValue();

            if (op == Operator.EQ) {
                return longValue == value;
            } else if (op == Operator.NE) {
                return longValue != value;
            } else if (op == Operator.GE) {
                return longValue >= value;
            } else if (op == Operator.GT) {
                return longValue > value;
            } else if (op == Operator.LE) {
                return longValue <= value;
            } else if (op == Operator.LT) {
                return longValue < value;
            }

            return false;
        }

    }

    static class MatchSegement implements Filter {

        private final String   propertyName;
        private final String   startsWithValue;
        private final String   endsWithValue;
        private final String[] containsValues;
        private final int      minLength;
        private final boolean  not;

        public MatchSegement(String propertyName, String startsWithValue, String endsWithValue, String[] containsValues,
                             boolean not){
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

    static class RlikeSegement implements Filter {

        private final String  propertyName;
        private final Pattern pattern;
        private final boolean not;

        public RlikeSegement(String propertyName, String pattern, boolean not){
            this.propertyName = propertyName;
            this.pattern = Pattern.compile(pattern);
            this.not = not;
        }

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

    static class StringOpSegement implements Filter {

        private final String   propertyName;
        private final String   value;
        private final Operator op;

        public StringOpSegement(String propertyName, String value, Operator op){
            this.propertyName = propertyName;
            this.value = value;
            this.op = op;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, false);

            if (op == Operator.EQ) {
                return value.equals(propertyValue);
            } else if (op == Operator.NE) {
                return !value.equals(propertyValue);
            }

            if (propertyValue == null) {
                return false;
            }

            int compareResult = value.compareTo(propertyValue.toString());
            if (op == Operator.GE) {
                return compareResult <= 0;
            } else if (op == Operator.GT) {
                return compareResult < 0;
            } else if (op == Operator.LE) {
                return compareResult >= 0;
            } else if (op == Operator.LT) {
                return compareResult > 0;
            }

            return false;
        }
    }

    enum Operator {
                   EQ, NE, GT, GE, LT, LE, LIKE, NOT_LIKE, RLIKE, NOT_RLIKE, IN, NOT_IN, BETWEEN, NOT_BETWEEN
    }

    static public class FilterSegement implements Segement {

        private final Filter filter;

        public FilterSegement(Filter filter){
            super();
            this.filter = filter;
        }

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

                    if (filter.apply(path, rootObject, currentObject, item)) {
                        items.add(item);
                    }
                }

                return items;
            }

            if (filter.apply(path, rootObject, currentObject, currentObject)) {
                return currentObject;
            }

            return null;
        }
    }

    interface Filter {

        boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item);
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

        if (currentObject.getClass().isArray()) {
            int arrayLenth = Array.getLength(currentObject);

            if (index >= 0) {
                if (index < arrayLenth) {
                    return Array.get(currentObject, index);
                }
                return null;
            } else {
                if (Math.abs(index) <= arrayLenth) {
                    return Array.get(currentObject, arrayLenth + index);
                }
                return null;
            }
        }

        throw new UnsupportedOperationException();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean setArrayItem(JSONPath path, Object currentObject, int index, Object value) {
        if (currentObject instanceof List) {
            List list = (List) currentObject;
            if (index >= 0) {
                list.set(index, value);
            } else {
                list.set(list.size() + index, value);
            }
            return true;
        }

        if (currentObject.getClass().isArray()) {
            int arrayLenth = Array.getLength(currentObject);

            if (index >= 0) {
                if (index < arrayLenth) {
                    Array.set(currentObject, index, value);
                }
            } else {
                if (Math.abs(index) <= arrayLenth) {
                    Array.set(currentObject, arrayLenth + index, value);
                }
            }

            return true;
        }

        throw new UnsupportedOperationException();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Collection<Object> getPropertyValues(final Object currentObject) {
        final Class<?> currentClass = currentObject.getClass();

        JavaBeanSerializer beanSerializer = getJavaBeanSerializer(currentClass);

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

    static boolean eq(Object a, Object b) {
        if (a == b) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        if (a.getClass() == b.getClass()) {
            return a.equals(b);
        }

        if (a instanceof Number) {
            if (b instanceof Number) {
                return eqNotNull((Number) a, (Number) b);
            }

            return false;
        }

        return a.equals(b);
    }

    @SuppressWarnings("rawtypes")
    static boolean eqNotNull(Number a, Number b) {
        Class clazzA = a.getClass();
        boolean isIntA = isInt(clazzA);

        Class clazzB = a.getClass();
        boolean isIntB = isInt(clazzB);

        if (isIntA && isIntB) {
            return a.longValue() == b.longValue();
        }

        boolean isDoubleA = isDouble(clazzA);
        boolean isDoubleB = isDouble(clazzB);

        if ((isDoubleA && isDoubleB) || (isDoubleA && isIntA) || (isDoubleB && isIntA)) {
            return a.doubleValue() == b.doubleValue();
        }

        return false;
    }

    protected static boolean isDouble(Class<?> clazzA) {
        return clazzA == Float.class || clazzA == Double.class;
    }

    protected static boolean isInt(Class<?> clazzA) {
        return clazzA == Byte.class || clazzA == Short.class || clazzA == Integer.class || clazzA == Long.class;
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

        JavaBeanSerializer beanSerializer = getJavaBeanSerializer(currentClass);
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected boolean setPropertyValue(Object parent, String name, Object value) {
        if (parent instanceof Map) {
            ((Map) parent).put(name, value);
            return true;
        }

        if (parent instanceof List) {
            for (Object element : (List) parent) {
                if (element == null) {
                    continue;
                }
                setPropertyValue(element, name, value);
            }
            return true;
        }

        ObjectDeserializer derializer = parserConfig.getDeserializer(parent.getClass());

        JavaBeanDeserializer beanDerializer = null;
        if (derializer instanceof JavaBeanDeserializer) {
            beanDerializer = (JavaBeanDeserializer) derializer;
        } else if (derializer instanceof ASMJavaBeanDeserializer) {
            beanDerializer = ((ASMJavaBeanDeserializer) derializer).getInnterSerializer();
        }

        if (beanDerializer != null) {
            FieldDeserializer fieldDeserializer = beanDerializer.getFieldDeserializer(name);
            if (fieldDeserializer == null) {
                return false;
            }

            fieldDeserializer.setValue(parent, value);
            return true;
        }

        throw new UnsupportedOperationException();
    }

    protected JavaBeanSerializer getJavaBeanSerializer(final Class<?> currentClass) {
        JavaBeanSerializer beanSerializer = null;
        {
            ObjectSerializer serializer = serializeConfig.getObjectWriter(currentClass);
            if (serializer instanceof JavaBeanSerializer) {
                beanSerializer = (JavaBeanSerializer) serializer;
            } else if (serializer instanceof ASMJavaBeanSerializer) {
                beanSerializer = ((ASMJavaBeanSerializer) serializer).getJavaBeanSerializer();
            }
        }
        return beanSerializer;
    }

    @SuppressWarnings("rawtypes")
    int evalSize(Object currentObject) {
        if (currentObject == null) {
            return -1;
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
            int count = 0;

            for (Object value : ((Map) currentObject).values()) {
                if (value != null) {
                    count++;
                }
            }
            return count;
        }

        JavaBeanSerializer beanSerializer = getJavaBeanSerializer(currentObject.getClass());

        if (beanSerializer == null) {
            return -1;
        }

        try {
            List<Object> values = beanSerializer.getFieldValues(currentObject);

            int count = 0;
            for (int i = 0; i < values.size(); ++i) {
                if (values.get(i) != null) {
                    count++;
                }
            }
            return count;
        } catch (Exception e) {
            throw new JSONException("evalSize error : " + path, e);
        }
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        serializer.write(path);
    }
}
