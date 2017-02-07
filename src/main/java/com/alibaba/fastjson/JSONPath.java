package com.alibaba.fastjson;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.FieldSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.IOUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 * @since 1.2.0
 */
public class JSONPath implements JSONAware {

    private static int                             CACHE_SIZE = 1024;
    private static ConcurrentMap<String, JSONPath> pathCache  = new ConcurrentHashMap<String, JSONPath>(128, 0.75f, 1);

    private final String                           path;
    private Segement[]                             segments;

    private SerializeConfig                        serializeConfig;
    private ParserConfig                           parserConfig;

    public JSONPath(String path){
        this(path, SerializeConfig.getGlobalInstance(), ParserConfig.getGlobalInstance());
    }

    public JSONPath(String path, SerializeConfig serializeConfig, ParserConfig parserConfig){
        if (path == null || path.length() == 0) {
            throw new JSONPathException("json-path can not be null or empty");
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
            throw new JSONException("unsupported array put operation. " + resultClass);
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
    
    public boolean remove(Object rootObject) {
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
            return propertySegement.remove(this, parentObject);
        }

        if (lastSegement instanceof ArrayAccessSegement) {
            return ((ArrayAccessSegement) lastSegement).remove(this, parentObject);
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
//            if (i == segments.length - 1) {
//                parentObject = currentObject;
//                break;
//            }
//            
            parentObject = currentObject;
            Segement segment = segments[i];
            currentObject = segment.eval(this, rootObject, currentObject);
            if (currentObject == null) {
                Segement nextSegement = null;
                if (i < segments.length - 1) {
                    nextSegement = segments[i + 1];
                }

                Object newObj = null;
                if (nextSegement instanceof PropertySegement) {
                    Class<?> parentClass = parentObject.getClass();
                    JavaBeanSerializer beanSerializer = getJavaBeanSerializer(parentClass);
                    if (beanSerializer != null) {
                        return false;
                    }

                    newObj = new JSONObject();   
                } else if (nextSegement instanceof ArrayAccessSegement) {
                    newObj = new JSONArray();
                }
                
                if (newObj != null) {
                    if (segment instanceof PropertySegement) {
                        PropertySegement propSegement = (PropertySegement) segment;
                        propSegement.setValue(this, parentObject, newObj);
                        currentObject = newObj;
                        continue;
                    } else if (segment instanceof ArrayAccessSegement) {
                        ArrayAccessSegement arrayAccessSegement = (ArrayAccessSegement) segment;
                        arrayAccessSegement.setValue(this, parentObject, newObj);
                        currentObject = newObj;
                        continue;
                    }
                }
                
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

    public static boolean set(Object rootObject, String path, Object value) {
        JSONPath jsonpath = compile(path);
        return jsonpath.set(rootObject, value);
    }
    
    public static boolean remove(Object root, String path) {
        JSONPath jsonpath = compile(path);
        return jsonpath.remove(root);
    }

    public static JSONPath compile(String path) {
        if (path == null) {
            throw new JSONPathException("jsonpath can not be null");
        }
        
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
    
    /**
     * @since 1.2.9
     * @param json
     * @param path
     * @return
     */
    public static Object read(String json, String path) {
        Object object = JSON.parse(json);
        JSONPath jsonpath = compile(path);
        return jsonpath.eval(object);
    }
    
    public static Map<String, Object> paths(Object javaObject) {
        return paths(javaObject, SerializeConfig.globalInstance);
    }
    
    public static Map<String, Object> paths(Object javaObject, SerializeConfig config) {
        Map<Object, String> values = new IdentityHashMap<Object, String>();
        paths(values, "/", javaObject, config);
        
        Map<String, Object> paths = new HashMap<String, Object>();
        for (Map.Entry<Object, String> entry : values.entrySet()) {
            paths.put(entry.getValue(), entry.getKey());
        }
        return paths;
    }

    @SuppressWarnings("rawtypes")
    private static void paths(Map<Object, String> paths, String parent, Object javaObject, SerializeConfig config) {
        if (javaObject == null) {
            return;
        }
        
        if (paths.containsKey(javaObject)) {
            return;
        }
        
        paths.put(javaObject, parent);
        
        if (javaObject instanceof Map) {
            Map map = (Map) javaObject;

            for (Object entryObj : map.entrySet()) {
                Map.Entry entry = (Map.Entry) entryObj;
                Object key = entry.getKey();
                
                if (key instanceof String) {
                    String path = parent.equals("/") ?  "/" + key : parent + "/" + key;
                    paths(paths, path, entry.getValue(), config);
                }
            }
            return;
        }

        if (javaObject instanceof Collection) {
            Collection collection = (Collection) javaObject;

            int i = 0;
            for (Object item : collection) {
                String path = parent.equals("/") ?  "/" + i : parent + "/" + i;
                paths(paths, path, item, config);
                ++i;
            }
            
            return;
        }

        Class<?> clazz = javaObject.getClass();

        if (clazz.isArray()) {
            int len = Array.getLength(javaObject);

            for (int i = 0; i < len; ++i) {
                Object item = Array.get(javaObject, i);
                
                String path = parent.equals("/") ?  "/" + i : parent + "/" + i;
                paths(paths, path, item, config);
            }
            
            return;
        }

        if (ParserConfig.isPrimitive(clazz) || clazz.isEnum()) {
            return;
        }

        ObjectSerializer serializer = config.getObjectWriter(clazz);
        if (serializer instanceof JavaBeanSerializer) {
            JavaBeanSerializer javaBeanSerializer = (JavaBeanSerializer) serializer;
            
            try {
                Map<String, Object> fieldValues = javaBeanSerializer.getFieldValuesMap(javaObject);
                for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                    String key = entry.getKey();
                    
                    if (key instanceof String) {
                        String path = parent.equals("/") ?  "/" + key : parent + "/" + key;
                        paths(paths, path, entry.getValue(), config);
                    }
                }
            } catch (Exception e) {
                throw new JSONException("toJSON error", e);
            }
            return;
        }
        
        return;
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
                    return new PropertySegement(Character.toString(ch), false);
                }
            }
            while (!isEOF()) {
                skipWhitespace();

                if (ch == '$') {
                    next();
                    continue;
                }

                if (ch == '.' || ch == '/') {
                    int c0 = ch;
                    boolean deep = false;
                    next();
                    if (c0 == '.' && ch == '.') {
                        next();
                        deep = true;
                    }
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

                    return new PropertySegement(propertyName, deep);
                }

                if (ch == '[') {
                    return parseArrayAccess(true);
                }

                if (level == 0) {
                    String propertyName = readName();

                    return new PropertySegement(propertyName, false);
                }

                throw new UnsupportedOperationException();
            }

            return null;
        }

        public final void skipWhitespace() {
            for (;;) {
                if (ch <= ' ' && (ch == ' ' || ch == '\r' || ch == '\n' || ch == '\t' || ch == '\f' || ch == '\b')) {
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

                    List<Object> valueList = new JSONArray();
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
                    double doubleValue = 0D;
                    if (ch == '.') {
                        doubleValue = readDoubleValue(value);
                        
                    }

                    if (predicateFlag) {
                        accept(')');
                    }
                    
                    if (acceptBracket) {
                        accept(']');
                    }

                    if (doubleValue == 0) {
                        return new FilterSegement(new IntOpSegement(propertyName, value, op));
                    } else {
                        return new FilterSegement(new DoubleOpSegement(propertyName, doubleValue, op));    
                    }
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
                } else if (ch == 't') {
                    String name = readName();
                    
                    if ("true".equals(name)) {
                        if (predicateFlag) {
                            accept(')');
                        }
                        accept(']');

                        if (op == Operator.EQ) {
                            return new FilterSegement(new ValueSegment(propertyName, Boolean.TRUE, true));
                        }

                        if (op == Operator.NE) {
                            return new FilterSegement(new ValueSegment(propertyName, Boolean.TRUE, false));
                        }

                        throw new UnsupportedOperationException();
                    }
                } else if (ch == 'f') {
                    String name = readName();
                    
                    if ("false".equals(name)) {
                        if (predicateFlag) {
                            accept(')');
                        }
                        accept(']');

                        if (op == Operator.EQ) {
                            return new FilterSegement(new ValueSegment(propertyName, Boolean.FALSE, true));
                        }

                        if (op == Operator.NE) {
                            return new FilterSegement(new ValueSegment(propertyName, Boolean.FALSE, false));
                        }

                        throw new UnsupportedOperationException();
                    }
                }

                throw new UnsupportedOperationException();
                // accept(')');
            }

            int start = pos - 1;
            while (ch != ']' && ch != '/' && !isEOF()) {
                if (ch == '.' //
                        && (!predicateFlag) // 
                        && !predicateFlag) {
                    break;
                }
                
                if (ch == '\\') {
                    next();
                }
                next();
            }
            
            int end;
            if (acceptBracket) {
                end = pos - 1;
            } else {
                if (ch == '/' || ch == '.') {
                    end = pos - 1;
                } else {
                    end = pos;
                }
            }
            
            String text = path.substring(start, end);
            
            if (text.indexOf("\\.") != -1) {
                String propName = text.replaceAll("\\\\\\.","\\.");
                return new PropertySegement(propName, false);
            }

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
        
        protected double readDoubleValue(long longValue) {
            int beginIndex = pos - 1;

            next();
            while (ch >= '0' && ch <= '9') {
                next();
            }

            int endIndex = pos - 1;
            String text = path.substring(beginIndex, endIndex);
            double value = Double.parseDouble(text);
            value += longValue;
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

            if (ch != '\\' && !IOUtils.firstIdentifier(ch)) {
                throw new JSONPathException("illeal jsonpath syntax. " + path);
            }

            StringBuilder buf = new StringBuilder();
            while (!isEOF()) {
                if (ch == '\\') {
                    next();
                    buf.append(ch);
                    if (isEOF()) {
                        break;
                    }
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
            if (path == null || path.length() == 0) {
                throw new IllegalArgumentException();
            }

            Segement[] segements = new Segement[8];

            for (;;) {
                Segement segment = readSegement();
                if (segment == null) {
                    break;
                }
                
                if (level == segements.length) {
                    Segement[] t = new Segement[level * 3 / 2];
                    System.arraycopy(segements, 0, t, 0, level);
                    segements = t;
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
                    return new PropertySegement(propertyName, false);
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
                    if (str.length() == 0) {
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


    static class SizeSegement implements Segement {

        public final static SizeSegement instance = new SizeSegement();

        public Integer eval(JSONPath path, Object rootObject, Object currentObject) {
            return path.evalSize(currentObject);
        }
    }

    static class PropertySegement implements Segement {

        private final String propertyName;
        private final boolean deep;

        public PropertySegement(String propertyName, boolean deep){
            this.propertyName = propertyName;
            this.deep = deep;
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            if (deep) {
                List<Object> results = new ArrayList<Object>();
                path.deepScan(currentObject, propertyName, results);
                return results;
            } else {
                return path.getPropertyValue(currentObject, propertyName, true);
            }
        }

        public void setValue(JSONPath path, Object parent, Object value) {
            path.setPropertyValue(parent, propertyName, value);
        }
        
        public boolean remove(JSONPath path, Object parent) {
            return path.removePropertyValue(parent, propertyName);
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
        
        public boolean remove(JSONPath path, Object currentObject) {
            return path.removeArrayItem(path, currentObject, index);
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

            int array_size = (end - start) / step + 1;
            if (array_size == -1) {
                return null;
            }

            List<Object> items = new ArrayList<Object>(array_size);
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
    
    static class ValueSegment implements Filter {
        private final String propertyName;
        private final Object value;
        private boolean eq = true;
        
        public ValueSegment(String propertyName, Object value, boolean eq){
            if (value == null) {
                throw new IllegalArgumentException("value is null");
            }
            this.propertyName = propertyName;
            this.value = value;
            this.eq = eq;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, false);
            boolean result = value.equals(propertyValue);
            if (!eq) {
                result = !result;
            }
            return result;
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
    
    static class DoubleOpSegement implements Filter {

        private final String   propertyName;
        private final double     value;
        private final Operator op;

        public DoubleOpSegement(String propertyName, double value, Operator op){
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

            double doubleValue = ((Number) propertyValue).doubleValue();

            if (op == Operator.EQ) {
                return doubleValue == value;
            } else if (op == Operator.NE) {
                return doubleValue != value;
            } else if (op == Operator.GE) {
                return doubleValue >= value;
            } else if (op == Operator.GT) {
                return doubleValue > value;
            } else if (op == Operator.LE) {
                return doubleValue <= value;
            } else if (op == Operator.LT) {
                return doubleValue < value;
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

            List<Object> items = new JSONArray();

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

        Class<?> clazz = currentObject.getClass();
        if (clazz.isArray()) {
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

        throw new JSONPathException("unsupported set operation." + clazz);
    }

    @SuppressWarnings("rawtypes")
    public boolean removeArrayItem(JSONPath path, Object currentObject, int index) {
        if (currentObject instanceof List) {
            List list = (List) currentObject;
            if (index >= 0) {
                if (index >= list.size()) {
                    return false;
                }
                list.remove(index);
            } else {
                int newIndex = list.size() + index;
                
                if (newIndex < 0) {
                    return false;
                }
                
                list.remove(newIndex);
            }
            return true;
        }

        Class<?> clazz = currentObject.getClass();
        throw new JSONPathException("unsupported set operation." + clazz);
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

        Class clazzB = b.getClass();
        boolean isIntB = isInt(clazzB);
        
        if (a instanceof BigDecimal) {
            BigDecimal decimalA = (BigDecimal) a;
            
            if (isIntB) {
                return decimalA.equals(BigDecimal.valueOf(b.longValue()));
            }
        }

        if (isIntA) {
            if (isIntB) {
                return a.longValue() == b.longValue();
            }
            
            if (b instanceof BigInteger) {
                BigInteger bigIntB = (BigInteger) a;
                BigInteger bigIntA = BigInteger.valueOf(a.longValue());
                
                return bigIntA.equals(bigIntB);
            }
        }
        
        if (isIntB) {
            if (a instanceof BigInteger) {
                BigInteger bigIntA = (BigInteger) a;
                BigInteger bigIntB = BigInteger.valueOf(b.longValue());
                
                return bigIntA.equals(bigIntB);
            }
        }
        

        boolean isDoubleA = isDouble(clazzA);
        boolean isDoubleB = isDouble(clazzB);

        if ((isDoubleA && isDoubleB) || (isDoubleA && isIntB) || (isDoubleB && isIntA)) {
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
            Object val = map.get(propertyName);
            
            if (val == null && "size".equals(propertyName)) {
                val = map.size();
            }
            
            return val;
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
            
            if ("size".equals(propertyName)) {
                return list.size();
            }

            List<Object> fieldValues = new JSONArray(list.size());

            for (int i = 0; i < list.size(); ++i) {
                Object obj = list.get(i);
                Object itemValue = getPropertyValue(obj, propertyName, strictMode);
                fieldValues.add(itemValue);
            }

            return fieldValues;
        }
        
        if (currentObject instanceof Enum) {
            Enum e = (Enum) currentObject;
            if ("name".equals(propertyName)) {
                return e.name();
            }
            
            if ("ordinal".equals(propertyName)) {
                return e.ordinal();
            }
        }
        
        if (currentObject instanceof Calendar) {
            Calendar e = (Calendar) currentObject;
            
            if ("year".equals(propertyName)) {
                return e.get(Calendar.YEAR);
            }
            
            if ("month".equals(propertyName)) {
                return e.get(Calendar.MONTH);
            }
            
            if ("day".equals(propertyName)) {
                return e.get(Calendar.DAY_OF_MONTH);
            }
            
            if ("hour".equals(propertyName)) {
                return e.get(Calendar.HOUR_OF_DAY);
            }
            
            if ("minute".equals(propertyName)) {
                return e.get(Calendar.MINUTE);
            }
            
            if ("second".equals(propertyName)) {
                return e.get(Calendar.SECOND);
            }
        }
        
        throw new JSONPathException("jsonpath error, path " + path + ", segement " + propertyName);
    }
    
    @SuppressWarnings("rawtypes")
    protected void deepScan(final Object currentObject, final String propertyName, List<Object> results) {
        if (currentObject == null) {
            return;
        }

        if (currentObject instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) currentObject;
            
            if (map.containsKey(propertyName)) {
                Object val = map.get(propertyName);
                results.add(val);
                return;
            }
            
            for (Object val : map.values()) {
                deepScan(val, propertyName, results);
            }
            return;
        }

        final Class<?> currentClass = currentObject.getClass();

        JavaBeanSerializer beanSerializer = getJavaBeanSerializer(currentClass);
        if (beanSerializer != null) {
            try {
                FieldSerializer fieldDeser = beanSerializer.getFieldSerializer(propertyName);
                if (fieldDeser != null) {
                    try {
                        Object val = fieldDeser.getPropertyValueDirect(currentObject);
                        results.add(val);
                    } catch (InvocationTargetException ex) {
                        throw new JSONException("getFieldValue error." + propertyName, ex);
                    } catch (IllegalAccessException ex) {
                        throw new JSONException("getFieldValue error." + propertyName, ex);
                    }
                    return;
                }
                List<Object> fieldValues = beanSerializer.getFieldValues(currentObject);
                for (Object val : fieldValues) {
                    deepScan(val, propertyName, results);
                }
                return;
            } catch (Exception e) {
                throw new JSONPathException("jsonpath error, path " + path + ", segement " + propertyName, e);
            }
        }

        if (currentObject instanceof List) {
            List list = (List) currentObject;

            for (int i = 0; i < list.size(); ++i) {
                Object val = list.get(i);
                deepScan(val, propertyName, results);
            }
            return;
        }
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
    
    @SuppressWarnings({"rawtypes" })
    protected boolean removePropertyValue(Object parent, String name) {
        if (parent instanceof Map) {
            Object origin = ((Map) parent).remove(name);
            return origin != null;
        }

        ObjectDeserializer derializer = parserConfig.getDeserializer(parent.getClass());

        JavaBeanDeserializer beanDerializer = null;
        if (derializer instanceof JavaBeanDeserializer) {
            beanDerializer = (JavaBeanDeserializer) derializer;
        }

        if (beanDerializer != null) {
            FieldDeserializer fieldDeserializer = beanDerializer.getFieldDeserializer(name);
            if (fieldDeserializer == null) {
                return false;
            }

            fieldDeserializer.setValue(parent, null);
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
            return beanSerializer.getSize(currentObject);
        } catch (Exception e) {
            throw new JSONPathException("evalSize error : " + path, e);
        }
    }

    public String toJSONString() {
        return JSON.toJSONString(path);
    }
}
