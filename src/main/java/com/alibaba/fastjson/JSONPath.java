package com.alibaba.fastjson;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.FieldSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wenshao[szujobs@hotmail.com]
 * @since 1.2.0
 */
public class JSONPath implements JSONAware {
    private static ConcurrentMap<String, JSONPath> pathCache  = new ConcurrentHashMap<String, JSONPath>(128, 0.75f, 1);

    private final String                           path;
    private Segment[]                              segments;
    private boolean                                hasRefSegment;

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
            this.segments = new Segment[] { WildCardSegment.instance };
        } else {
            JSONPathParser parser = new JSONPathParser(path);
            this.segments = parser.explain();
            this.hasRefSegment = parser.hasRefSegment;
        }
    }

    public boolean isRef() {
        init();
        for (int i = 0; i < segments.length; ++i) {
            Segment segment = segments[i];
            Class segmentType = segment.getClass();
            if (segmentType == ArrayAccessSegment.class
                    || segmentType == PropertySegment.class) {
                continue;
            }
            return false;
        }
        return true;
    }

    public Object eval(Object rootObject) {
        if (rootObject == null) {
            return null;
        }

        init();

        Object currentObject = rootObject;
        for (int i = 0; i < segments.length; ++i) {
            Segment segment = segments[i];
            currentObject = segment.eval(this, rootObject, currentObject);
        }
        return currentObject;
    }

    public Object extract(DefaultJSONParser parser) {
        if (parser == null) {
            return null;
        }

        init();

        if (hasRefSegment) {
            Object root = parser.parse();
            return this.eval(root);
        }

        if (segments.length == 0) {
            return parser.parse();
        }

        Context context = null;
        for (int i = 0; i < segments.length; ++i) {
            Segment segment = segments[i];
            boolean last = i == segments.length - 1;

            if (context != null && context.object != null) {
                context.object = segment.eval(this, null, context.object);
                continue;
            }

            boolean eval;

            if (!last) {
                Segment nextSegment = segments[i + 1];
                if (segment instanceof PropertySegment
                        && ((PropertySegment) segment).deep
                        && (nextSegment instanceof ArrayAccessSegment
                            || nextSegment instanceof MultiIndexSegment
                            || nextSegment instanceof MultiPropertySegment
                            || nextSegment instanceof SizeSegment
                            || nextSegment instanceof PropertySegment
                            || nextSegment instanceof FilterSegment))
                {
                    eval = true;
                } else if (nextSegment instanceof ArrayAccessSegment
                        && ((ArrayAccessSegment) nextSegment).index < 0) {
                    eval = true;
                } else if (nextSegment instanceof FilterSegment) {
                    eval = true;
                } else if (segment instanceof WildCardSegment) {
                    eval = true;
                } else {
                    eval = false;
                }
            } else {
                eval = true;
            }

            context = new Context(context, eval);
            segment.extract(this, parser, context);
        }

        return context.object;
    }

    private static class Context {
        final Context parent;
        final boolean eval;
        Object object;

        public Context(Context parent, boolean eval) {
            this.parent = parent;
            this.eval = eval;
        }
    }

    public boolean contains(Object rootObject) {
        if (rootObject == null) {
            return false;
        }

        init();

        Object currentObject = rootObject;
        for (int i = 0; i < segments.length; ++i) {
            Object parentObject = currentObject;
            currentObject = segments[i].eval(this, rootObject, currentObject);
            if (currentObject == null) {
                return false;
            }

            if (currentObject == Collections.EMPTY_LIST && parentObject instanceof List) {
                return ((List) parentObject).contains(currentObject);
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

    /**
     * Extract keySet or field names from rootObject on this JSONPath.
     * 
     * @param rootObject Can be a map or custom object. Array and Collection are not supported.
     * @return Set of keys, or <code>null</code> if not supported.
     */
    public Set<?> keySet(Object rootObject) {
        if (rootObject == null) {
            return null;
        }

        init();

        Object currentObject = rootObject;
        for (int i = 0; i < segments.length; ++i) {
            currentObject = segments[i].eval(this, rootObject, currentObject);
        }

        return evalKeySet(currentObject);
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

        Segment lastSegment = segments[segments.length - 1];
        if (lastSegment instanceof PropertySegment) {
            PropertySegment propertySegment = (PropertySegment) lastSegment;
            propertySegment.setValue(this, parentObject, newResult);
            return;
        }

        if (lastSegment instanceof ArrayAccessSegment) {
            ((ArrayAccessSegment) lastSegment).setValue(this, parentObject, newResult);
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

        Segment lastSegment = segments[segments.length - 1];
        for (int i = 0; i < segments.length; ++i) {
            if (i == segments.length - 1) {
                parentObject = currentObject;
                break;
            }
            Segment segement = segments[i];
            if (i == segments.length - 2
                    && lastSegment instanceof FilterSegment
                    && segement instanceof PropertySegment
            ) {
                FilterSegment filterSegment = (FilterSegment) lastSegment;

                if (currentObject instanceof List) {
                    PropertySegment propertySegment = (PropertySegment) segement;
                    List list = (List) currentObject;

                    for (Iterator it = list.iterator();it.hasNext();) {
                        Object item = it.next();
                        Object result = propertySegment.eval(this, rootObject, item);
                        if (result instanceof Iterable) {
                            filterSegment.remove(this, rootObject, result);
                        } else if (result instanceof Map) {
                            if (filterSegment.filter.apply(this, rootObject, currentObject, result)) {
                                it.remove();
                            }
                        }
                    }
                    return true;
                } else if (currentObject instanceof Map) {
                    PropertySegment propertySegment = (PropertySegment) segement;
                    Object result = propertySegment.eval(this, rootObject, currentObject);
                    if (result == null) {
                        return false;
                    }
                    if (result instanceof Map
                            && filterSegment.filter.apply(this, rootObject, currentObject, result)) {
                        propertySegment.remove(this, currentObject);
                        return true;
                    }
                }
            }

            currentObject = segement.eval(this, rootObject, currentObject);
            if (currentObject == null) {
                break;
            }
        }

        if (parentObject == null) {
            return false;
        }


        if (lastSegment instanceof PropertySegment) {
            PropertySegment propertySegment = (PropertySegment) lastSegment;

            if (parentObject instanceof Collection) {
                if (segments.length > 1) {
                    Segment parentSegment = segments[segments.length - 2];
                    if (parentSegment instanceof RangeSegment || parentSegment instanceof MultiIndexSegment) {
                        Collection collection = (Collection) parentObject;
                        boolean removedOnce = false;
                        for (Object item : collection) {
                            boolean removed = propertySegment.remove(this, item);
                            if (removed) {
                                removedOnce = true;
                            }
                        }
                        return removedOnce;
                    }
                }
            }
            return propertySegment.remove(this, parentObject);
        }

        if (lastSegment instanceof ArrayAccessSegment) {
            return ((ArrayAccessSegment) lastSegment).remove(this, parentObject);
        }

        if (lastSegment instanceof FilterSegment) {
            FilterSegment filterSegment = (FilterSegment) lastSegment;
            return filterSegment.remove(this, rootObject, parentObject);
        }

        throw new UnsupportedOperationException();
    }

    public boolean set(Object rootObject, Object value) {
        return set(rootObject, value, true);
    }

    public boolean set(Object rootObject, Object value, boolean p) {
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
            Segment segment = segments[i];
            currentObject = segment.eval(this, rootObject, currentObject);
            if (currentObject == null) {
                Segment nextSegment = null;
                if (i < segments.length - 1) {
                    nextSegment = segments[i + 1];
                }

                Object newObj = null;
                if (nextSegment instanceof PropertySegment) {
                    JavaBeanDeserializer beanDeserializer = null;
                    Class<?> fieldClass = null;
                    if (segment instanceof PropertySegment) {
                        String propertyName = ((PropertySegment) segment).propertyName;
                        Class<?> parentClass = parentObject.getClass();
                        JavaBeanDeserializer parentBeanDeserializer = getJavaBeanDeserializer(parentClass);
                        if (parentBeanDeserializer != null) {
                            FieldDeserializer fieldDeserializer = parentBeanDeserializer.getFieldDeserializer(propertyName);
                            fieldClass = fieldDeserializer.fieldInfo.fieldClass;
                            beanDeserializer = getJavaBeanDeserializer(fieldClass);
                        }
                    }

                    if (beanDeserializer != null) {

                        if (beanDeserializer.beanInfo.defaultConstructor != null) {
                            newObj = beanDeserializer.createInstance(null, fieldClass);
                        } else {
                            return false;
                        }
                    } else {
                        newObj = new JSONObject();
                    }
                } else if (nextSegment instanceof ArrayAccessSegment) {
                    newObj = new JSONArray();
                }
                
                if (newObj != null) {
                    if (segment instanceof PropertySegment) {
                        PropertySegment propSegement = (PropertySegment) segment;
                        propSegement.setValue(this, parentObject, newObj);
                        currentObject = newObj;
                        continue;
                    } else if (segment instanceof ArrayAccessSegment) {
                        ArrayAccessSegment arrayAccessSegement = (ArrayAccessSegment) segment;
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

        Segment lastSegment = segments[segments.length - 1];
        if (lastSegment instanceof PropertySegment) {
            PropertySegment propertySegment = (PropertySegment) lastSegment;
            propertySegment.setValue(this, parentObject, value);
            return true;
        }

        if (lastSegment instanceof ArrayAccessSegment) {
            return ((ArrayAccessSegment) lastSegment).setValue(this, parentObject, value);
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

    /**
     * Compile jsonPath and use it to extract keySet or field names from rootObject.
     * 
     * @param rootObject Can be a map or custom object. Array and Collection are not supported.
     * @param path JSONPath string to be compiled.
     * @return Set of keys, or <code>null</code> if not supported.
     */
    public static Set<?> keySet(Object rootObject, String path) {
        JSONPath jsonpath = compile(path);
        Object result = jsonpath.eval(rootObject);
        return jsonpath.evalKeySet(result);
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
            if (pathCache.size() < 1024) {
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
        return compile(path)
                .eval(
                        JSON.parse(json)
                );
    }

    /**
     * @since 1.2.51
     * @param json
     * @param path
     * @return
     */
    public static Object extract(String json, String path, ParserConfig config, int features, Feature... optionFeatures) {
        features |= Feature.OrderedField.mask;
        DefaultJSONParser parser = new DefaultJSONParser(json, config, features);
        JSONPath jsonPath = compile(path);
        Object result = jsonPath.extract(parser);
        parser.lexer.close();
        return result;
    }

    public static Object extract(String json, String path) {
        return extract(json, path, ParserConfig.global, JSON.DEFAULT_PARSER_FEATURE);
    }

    public static Map<String, Object> paths(Object javaObject) {
        return paths(javaObject, SerializeConfig.globalInstance);
    }
    
    public static Map<String, Object> paths(Object javaObject, SerializeConfig config) {
        Map<Object, String> values = new IdentityHashMap<Object, String>();
        Map<String, Object> paths = new HashMap<String, Object>();

        paths(values, paths, "/", javaObject, config);
        return paths;
    }

    private static void paths(Map<Object, String> values, Map<String, Object> paths, String parent, Object javaObject, SerializeConfig config) {
        if (javaObject == null) {
            return;
        }

        String p = values.put(javaObject, parent);
        if (p != null) {
            boolean basicType =  javaObject instanceof String
                    || javaObject instanceof Number
                    || javaObject instanceof Date
                    || javaObject instanceof UUID;

            if (!basicType) {
                return;
            }
        }

        paths.put(parent, javaObject);

        if (javaObject instanceof Map) {
            Map map = (Map) javaObject;

            for (Object entryObj : map.entrySet()) {
                Map.Entry entry = (Map.Entry) entryObj;
                Object key = entry.getKey();

                if (key instanceof String) {
                    String path = parent.equals("/") ?  "/" + key : parent + "/" + key;
                    paths(values, paths, path, entry.getValue(), config);
                }
            }
            return;
        }

        if (javaObject instanceof Collection) {
            Collection collection = (Collection) javaObject;

            int i = 0;
            for (Object item : collection) {
                String path = parent.equals("/") ?  "/" + i : parent + "/" + i;
                paths(values, paths, path, item, config);
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
                paths(values, paths, path, item, config);
            }

            return;
        }

        if (ParserConfig.isPrimitive2(clazz) || clazz.isEnum()) {
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
                        paths(values, paths, path, entry.getValue(), config);
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
        private boolean      hasRefSegment;
        private static final String strArrayRegex = "\'\\s*,\\s*\'";
        private static final Pattern strArrayPatternx = Pattern.compile(strArrayRegex);

        public JSONPathParser(String path){
            this.path = path;
            next();
        }

        void next() {
            ch = path.charAt(pos++);
        }

        char getNextChar() {
            return path.charAt(pos);
        }

        boolean isEOF() {
            return pos >= path.length();
        }

        Segment readSegement() {
            if (level == 0 && path.length() == 1) {
                if (isDigitFirst(ch)) {
                    int index = ch - '0';
                    return new ArrayAccessSegment(index);
                } else if ((ch >= 'a' && ch <= 'z') || ((ch >= 'A' && ch <= 'Z'))) {
                    return new PropertySegment(Character.toString(ch), false);
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
                        if (path.length() > pos + 3
                                && ch == '['
                                && path.charAt(pos) == '*'
                                && path.charAt(pos + 1) == ']'
                                && path.charAt(pos + 2) == '.') {
                            next();
                            next();
                            next();
                            next();
                        }
                    }
                    if (ch == '*') {
                        if (!isEOF()) {
                            next();
                        }

                        return deep ? WildCardSegment.instance_deep : WildCardSegment.instance;
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

                            if ("size".equals(propertyName) || "length".equals(propertyName)) {
                                return SizeSegment.instance;
                            } else if ("max".equals(propertyName)) {
                                return MaxSegment.instance;
                            } else if ("min".equals(propertyName)) {
                                return MinSegment.instance;
                            } else if ("keySet".equals(propertyName)) {
                                return KeySetSegment.instance;
                            }

                            throw new JSONPathException("not support jsonpath : " + path);
                        }

                        throw new JSONPathException("not support jsonpath : " + path);
                    }

                    return new PropertySegment(propertyName, deep);
                }

                if (ch == '[') {
                    return parseArrayAccess(true);
                }

                if (level == 0) {
                    String propertyName = readName();

                    return new PropertySegment(propertyName, false);
                }

                throw new JSONPathException("not support jsonpath : " + path);
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

        Segment parseArrayAccess(boolean acceptBracket) {
            Object object = parseArrayAccessFilter(acceptBracket);
            if (object instanceof Segment) {
                return ((Segment) object);
            }
            return new FilterSegment((Filter) object);
        }

        Object parseArrayAccessFilter(boolean acceptBracket) {
            if (acceptBracket) {
                accept('[');
            }

            boolean predicateFlag = false;
            int lparanCount = 0;

            if (ch == '?') {
                next();
                accept('(');
                lparanCount++;
                while (ch == '(') {
                    next();
                    lparanCount++;
                }
                predicateFlag = true;
            }

            if (predicateFlag || IOUtils.firstIdentifier(ch) || ch == '\\' || ch == '@') {
                boolean self = false;
                if (ch == '@') {
                    next();
                    accept('.');
                    self = true;
                }
                String propertyName = readName();

                skipWhitespace();

                if (predicateFlag && ch == ')') {
                    next();

                    Filter filter = new NotNullSegement(propertyName);
                    while (ch == ' ') {
                        next();
                    }

                    if (ch == '&' || ch == '|') {
                        filter = filterRest(filter);
                    }

                    if (acceptBracket) {
                        accept(']');
                    }
                    return filter;
                }

                if (acceptBracket && ch == ']') {
                    next();
                    Filter filter = new NotNullSegement(propertyName);
                    while (ch == ' ') {
                        next();
                    }

                    if (ch == '&' || ch == '|') {
                        filter = filterRest(filter);
                    }

                    accept(')');
                    if (predicateFlag) {
                        accept(')');
                    }

                    if (acceptBracket) {
                        accept(']');
                    }
                    return filter;
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
                        Filter filter = new IntBetweenSegement(propertyName
                                , TypeUtils.longExtractValue((Number) startValue)
                                , TypeUtils.longExtractValue((Number) endValue)
                                , not);
                        return filter;
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
                        Filter filter;
                        if (not) {
                            filter = new NotNullSegement(propertyName);
                        } else {
                            filter = new NullSegement(propertyName);
                        }

                        while (ch == ' ') {
                            next();
                        }

                        if (ch == '&' || ch == '|') {
                            filter = filterRest(filter);
                        }

                        accept(')');
                        if (predicateFlag) {
                            accept(')');
                        }

                        if (acceptBracket) {
                            accept(']');
                        }

                        return filter;
                    }

                    if (isInt) {
                        if (valueList.size() == 1) {
                            long value = TypeUtils.longExtractValue((Number) valueList.get(0));
                            Operator intOp = not ? Operator.NE : Operator.EQ;
                            Filter filter = new IntOpSegement(propertyName, value, intOp);
                            while (ch == ' ') {
                                next();
                            }

                            if (ch == '&' || ch == '|') {
                                filter = filterRest(filter);
                            }

                            accept(')');
                            if (predicateFlag) {
                                accept(')');
                            }

                            if (acceptBracket) {
                                accept(']');
                            }

                            return filter;
                        }

                        long[] values = new long[valueList.size()];
                        for (int i = 0; i < values.length; ++i) {
                            values[i] = TypeUtils.longExtractValue((Number) valueList.get(i));
                        }

                        Filter filter = new IntInSegement(propertyName, values, not);

                        while (ch == ' ') {
                            next();
                        }

                        if (ch == '&' || ch == '|') {
                            filter = filterRest(filter);
                        }

                        accept(')');
                        if (predicateFlag) {
                            accept(')');
                        }

                        if (acceptBracket) {
                            accept(']');
                        }

                        return filter;
                    }

                    if (isString) {
                        if (valueList.size() == 1) {
                            String value = (String) valueList.get(0);

                            Operator intOp = not ? Operator.NE : Operator.EQ;
                            Filter filter = new StringOpSegement(propertyName, value, intOp);

                            while (ch == ' ') {
                                next();
                            }

                            if (ch == '&' || ch == '|') {
                                filter = filterRest(filter);
                            }

                            accept(')');
                            if (predicateFlag) {
                                accept(')');
                            }

                            if (acceptBracket) {
                                accept(']');
                            }

                            return filter;
                        }

                        String[] values = new String[valueList.size()];
                        valueList.toArray(values);

                        Filter filter = new StringInSegement(propertyName, values, not);

                        while (ch == ' ') {
                            next();
                        }

                        if (ch == '&' || ch == '|') {
                            filter = filterRest(filter);
                        }

                        accept(')');
                        if (predicateFlag) {
                            accept(')');
                        }

                        if (acceptBracket) {
                            accept(']');
                        }

                        return filter;
                    }

                    if (isIntObj) {
                        Long[] values = new Long[valueList.size()];
                        for (int i = 0; i < values.length; ++i) {
                            Number item = (Number) valueList.get(i);
                            if (item != null) {
                                values[i] = TypeUtils.longExtractValue(item);
                            }
                        }

                        Filter filter = new IntObjInSegement(propertyName, values, not);

                        while (ch == ' ') {
                            next();
                        }

                        if (ch == '&' || ch == '|') {
                            filter = filterRest(filter);
                        }

                        accept(')');
                        if (predicateFlag) {
                            accept(')');
                        }

                        if (acceptBracket) {
                            accept(']');
                        }

                        return filter;
                    }

                    throw new UnsupportedOperationException();
                }

                if (ch == '\'' || ch == '"') {
                    String strValue = readString();

                    Filter filter = null;
                    if (op == Operator.RLIKE) {
                        filter = new RlikeSegement(propertyName, strValue, false);
                    } else if (op == Operator.NOT_RLIKE) {
                        filter = new RlikeSegement(propertyName, strValue, true);
                    } else  if (op == Operator.LIKE || op == Operator.NOT_LIKE) {
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
                            filter = new StringOpSegement(propertyName, strValue, op);
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
                                if (items.length == 1) {
                                    startsWithValue = items[0];
                                } else {
                                    containsValues = items;
                                }
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

                            filter = new MatchSegement(propertyName, startsWithValue, endsWithValue,
                                    containsValues, not);
                        }
                    } else {
                        filter = new StringOpSegement(propertyName, strValue, op);
                    }

                    while (ch == ' ') {
                        next();
                    }

                    if (ch == '&' || ch == '|') {
                        filter = filterRest(filter);
                    }

                    if (predicateFlag) {
                        accept(')');
                    }
                    
                    if (acceptBracket) {
                        accept(']');
                    }

                    return filter;
                }

                if (isDigitFirst(ch)) {
                    long value = readLongValue();
                    double doubleValue = 0D;
                    if (ch == '.') {
                        doubleValue = readDoubleValue(value);
                        
                    }

                    Filter filter;

                    if (doubleValue == 0) {
                        filter = new IntOpSegement(propertyName, value, op);
                    } else {
                        filter = new DoubleOpSegement(propertyName, doubleValue, op);
                    }

                    while (ch == ' ') {
                        next();
                    }

                    if (lparanCount > 1 && ch == ')') {
                        next();
                        lparanCount--;
                    }

                    if (ch == '&' || ch == '|') {
                        filter = filterRest(filter);
                    }

                    if (predicateFlag) {
                        lparanCount--;
                        accept(')');
                    }

                    if (acceptBracket) {
                        accept(']');
                    }

                    return filter;
                } else if (ch == '$') {
                    Segment segment = readSegement();
                    RefOpSegement filter = new RefOpSegement(propertyName, segment, op);
                    hasRefSegment = true;
                    while (ch == ' ') {
                        next();
                    }

                    if (predicateFlag) {
                        accept(')');
                    }

                    if (acceptBracket) {
                        accept(']');
                    }

                    return filter;
                } else if (ch == '/') {
                    int flags = 0;
                    StringBuilder regBuf = new StringBuilder();
                    for (;;) {
                        next();
                        if (ch == '/') {
                            next();
                            if (ch == 'i') {
                                next();
                                flags |= Pattern.CASE_INSENSITIVE;
                            }
                            break;
                        }

                        if (ch == '\\') {
                            next();
                            regBuf.append(ch);
                        } else {
                            regBuf.append(ch);
                        }
                    }

                    Pattern pattern = Pattern.compile(regBuf.toString(), flags);
                    RegMatchSegement filter = new RegMatchSegement(propertyName, pattern, op);

                    if (predicateFlag) {
                        accept(')');
                    }

                    if (acceptBracket) {
                        accept(']');
                    }

                    return filter;
                }

                if (ch == 'n') {
                    String name = readName();
                    if ("null".equals(name)) {
                        Filter filter = null;
                        if (op == Operator.EQ) {
                            filter = new NullSegement(propertyName);
                        } else if (op == Operator.NE) {
                            filter = new NotNullSegement(propertyName);
                        }

                        if (filter != null) {
                            while (ch == ' ') {
                                next();
                            }

                            if (ch == '&' || ch == '|') {
                                filter = filterRest(filter);
                            }
                        }

                        if (predicateFlag) {
                            accept(')');
                        }
                        accept(']');

                        if (filter != null) {
                            return filter;
                        }

                        throw new UnsupportedOperationException();
                    }
                } else if (ch == 't') {
                    String name = readName();
                    
                    if ("true".equals(name)) {
                        Filter filter = null;

                        if (op == Operator.EQ) {
                            filter = new ValueSegment(propertyName, Boolean.TRUE, true);
                        } else if (op == Operator.NE) {
                            filter = new ValueSegment(propertyName, Boolean.TRUE, false);
                        }

                        if (filter != null) {
                            while (ch == ' ') {
                                next();
                            }

                            if (ch == '&' || ch == '|') {
                                filter = filterRest(filter);
                            }
                        }

                        if (predicateFlag) {
                            accept(')');
                        }
                        accept(']');

                        if (filter != null) {
                            return filter;
                        }

                        throw new UnsupportedOperationException();
                    }
                } else if (ch == 'f') {
                    String name = readName();
                    
                    if ("false".equals(name)) {
                        Filter filter = null;

                        if (op == Operator.EQ) {
                            filter = new ValueSegment(propertyName, Boolean.FALSE, true);
                        } else if (op == Operator.NE) {
                            filter = new ValueSegment(propertyName, Boolean.FALSE, false);
                        }

                        if (filter != null) {
                            while (ch == ' ') {
                                next();
                            }

                            if (ch == '&' || ch == '|') {
                                filter = filterRest(filter);
                            }
                        }

                        if (predicateFlag) {
                            accept(')');
                        }
                        accept(']');

                        if (filter != null) {
                            return filter;
                        }

                        throw new UnsupportedOperationException();
                    }
                }

                throw new UnsupportedOperationException();
                // accept(')');
            }

            int start = pos - 1;
            char startCh = ch;
            while (ch != ']' && ch != '/' && !isEOF()) {
                if (ch == '.' //
                        && (!predicateFlag) // 
                        && !predicateFlag
                        && startCh != '\''
                ) {
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
                String propName;
                if (startCh == '\'' && text.length() > 2 && text.charAt(text.length() - 1) == startCh) {
                    propName = text.substring(1, text.length() - 1);
                } else {
                    propName = text.replaceAll("\\\\\\.", "\\.");
                    if (propName.indexOf("\\-") != -1) {
                        propName = propName.replaceAll("\\\\-", "-");
                    }
                }

                if (predicateFlag) {
                    accept(')');
                }

                return new PropertySegment(propName, false);
            }

            Segment segment = buildArraySegement(text);

            if (acceptBracket && !isEOF()) {
                accept(']');
            }

            return segment;
        }

        Filter filterRest(Filter filter) {
            boolean and = ch == '&';
            if ((ch == '&' && getNextChar() == '&') || (ch == '|' && getNextChar() == '|')) {
                next();
                next();

                boolean paren = false;
                if (ch == '(') {
                    paren = true;
                    next();
                }

                while (ch == ' ') {
                    next();
                }

                Filter right = (Filter) parseArrayAccessFilter(false);

                filter = new FilterGroup(filter, right, and);

                if (paren && ch == ')') {
                    next();
                }
            }
            return filter;
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
                if (ch == '~') {
                    next();
                    op = Operator.REG_MATCH;
                } else if (ch == '=') {
                    next();
                    op = Operator.EQ;
                } else {
                    op = Operator.EQ;
                }
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
                } else if ("nin".equalsIgnoreCase(name)) {
                    op = Operator.NOT_IN;
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

            if (ch != '\\' && !Character.isJavaIdentifierStart(ch)) {
                throw new JSONPathException("illeal jsonpath syntax. " + path);
            }

            StringBuilder buf = new StringBuilder();
            while (!isEOF()) {
                if (ch == '\\') {
                    next();
                    buf.append(ch);
                    if (isEOF()) {
                        return buf.toString();
                    }
                    next();
                    continue;
                }

                boolean identifierFlag = Character.isJavaIdentifierPart(ch);
                if (!identifierFlag) {
                    break;
                }
                buf.append(ch);
                next();
            }

            if (isEOF() && Character.isJavaIdentifierPart(ch)) {
                buf.append(ch);
            }

            return buf.toString();
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

        public Segment[] explain() {
            if (path == null || path.length() == 0) {
                throw new IllegalArgumentException();
            }

            Segment[] segments = new Segment[8];

            for (;;) {
                Segment segment = readSegement();
                if (segment == null) {
                    break;
                }

                if (segment instanceof PropertySegment) {
                    PropertySegment propertySegment = (PropertySegment) segment;
                    if ((!propertySegment.deep) && propertySegment.propertyName.equals("*")) {
                        continue;
                    }
                }

                if (level == segments.length) {
                    Segment[] t = new Segment[level * 3 / 2];
                    System.arraycopy(segments, 0, t, 0, level);
                    segments = t;
                }
                segments[level++] = segment;
            }

            if (level == segments.length) {
                return segments;
            }

            Segment[] result = new Segment[level];
            System.arraycopy(segments, 0, result, 0, level);
            return result;
        }

        Segment buildArraySegement(String indexText) {
            final int indexTextLen = indexText.length();
            final char firstChar = indexText.charAt(0);
            final char lastChar = indexText.charAt(indexTextLen - 1);

            int commaIndex = indexText.indexOf(',');

            if (indexText.length() > 2 && firstChar == '\'' && lastChar == '\'') {

                String propertyName = indexText.substring(1, indexTextLen - 1);

                if (commaIndex == -1 || !strArrayPatternx.matcher(indexText).find()) {
                    return new PropertySegment(propertyName, false);
                }

                String[] propertyNames = propertyName.split(strArrayRegex);
                return new MultiPropertySegment(propertyNames);
            }

            int colonIndex = indexText.indexOf(':');

            if (commaIndex == -1 && colonIndex == -1) {
                if (TypeUtils.isNumber(indexText)) {
                    try {
                        int index = Integer.parseInt(indexText);
                        return new ArrayAccessSegment(index);
                    }catch (NumberFormatException ex){
                        return new PropertySegment(indexText, false); // fix ISSUE-1208
                    }
                } else {
                    if (indexText.charAt(0) == '"' && indexText.charAt(indexText.length() - 1) == '"') {
                        indexText = indexText.substring(1, indexText.length() - 1);
                    }
                    return new PropertySegment(indexText, false);
                }
            }

            if (commaIndex != -1) {
                String[] indexesText = indexText.split(",");
                int[] indexes = new int[indexesText.length];
                for (int i = 0; i < indexesText.length; ++i) {
                    indexes[i] = Integer.parseInt(indexesText[i]);
                }
                return new MultiIndexSegment(indexes);
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
                return new RangeSegment(start, end, step);
            }

            throw new UnsupportedOperationException();
        }
    }

    interface Segment {

        Object eval(JSONPath path, Object rootObject, Object currentObject);
        void extract(JSONPath path, DefaultJSONParser parser, Context context);
    }


    static class SizeSegment implements Segment {

        public final static SizeSegment instance = new SizeSegment();

        public Integer eval(JSONPath path, Object rootObject, Object currentObject) {
            return path.evalSize(currentObject);
        }

        public void extract(JSONPath path, DefaultJSONParser parser, Context context) {
            throw new UnsupportedOperationException();
        }
    }

    static class MaxSegment implements Segment {

        public final static MaxSegment instance = new MaxSegment();

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            Object max = null;
            if (currentObject instanceof Collection) {
                Iterator iterator = ((Collection) currentObject).iterator();
                while (iterator.hasNext()) {
                    Object next = iterator.next();
                    if (next == null) {
                        continue;
                    }

                    if (max == null) {
                        max = next;
                    } else if (compare(max, next) < 0) {
                        max = next;
                    }
                }
            } else {
                throw new UnsupportedOperationException();
            }

            return max;
        }

        public void extract(JSONPath path, DefaultJSONParser parser, Context context) {
            throw new UnsupportedOperationException();
        }
    }

    static class MinSegment implements Segment {
        public final static MinSegment instance = new MinSegment();

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            Object min = null;
            if (currentObject instanceof Collection) {
                Iterator iterator = ((Collection) currentObject).iterator();
                while (iterator.hasNext()) {
                    Object next = iterator.next();
                    if (next == null) {
                        continue;
                    }

                    if (min == null) {
                        min = next;
                    } else if (compare(min, next) > 0) {
                        min = next;
                    }
                }
            } else {
                throw new UnsupportedOperationException();
            }

            return min;
        }

        public void extract(JSONPath path, DefaultJSONParser parser, Context context) {
            throw new UnsupportedOperationException();
        }
    }

    static int compare(Object a, Object b) {
        if (a.getClass() == b.getClass()) {
            return ((Comparable) a).compareTo(b);
        }

        Class typeA = a.getClass();
        Class typeB = b.getClass();

        if (typeA == BigDecimal.class) {
            if (typeB == Integer.class) {
                b = new BigDecimal((Integer) b);
            } else if (typeB == Long.class) {
                b = new BigDecimal((Long) b);
            } else if (typeB == Float.class) {
                b = new BigDecimal((Float) b);
            } else if (typeB == Double.class) {
                b = new BigDecimal((Double) b);
            }
        } else if (typeA == Long.class) {
            if (typeB == Integer.class) {
                b = new Long((Integer) b);
            } else if (typeB == BigDecimal.class) {
                a = new BigDecimal((Long) a);
            } else if (typeB == Float.class) {
                a = new Float((Long) a);
            } else if (typeB == Double.class) {
                a = new Double((Long) a);
            }
        } else if (typeA == Integer.class) {
            if (typeB == Long.class) {
                a = new Long((Integer) a);
            } else if (typeB == BigDecimal.class) {
                a = new BigDecimal((Integer) a);
            } else if (typeB == Float.class) {
                a = new Float((Integer) a);
            } else if (typeB == Double.class) {
                a = new Double((Integer) a);
            }
        } else if (typeA == Double.class) {
            if (typeB == Integer.class) {
                b = new Double((Integer) b);
            } else if (typeB == Long.class) {
                b = new Double((Long) b);
            } else if (typeB == Float.class) {
                b = new Double((Float) b);
            }
        } else if (typeA == Float.class) {
            if (typeB == Integer.class) {
                b = new Float((Integer) b);
            } else if (typeB == Long.class) {
                b = new Float((Long) b);
            } else if (typeB == Double.class) {
                a = new Double((Float) a);
            }
        }

        return ((Comparable) a).compareTo(b);
    }

    static class KeySetSegment implements Segment {

        public final static KeySetSegment instance = new KeySetSegment();

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            return path.evalKeySet(currentObject);
        }

        public void extract(JSONPath path, DefaultJSONParser parser, Context context) {
            throw new UnsupportedOperationException();
        }
    }

    static class PropertySegment implements Segment {

        private final String  propertyName;
        private final long    propertyNameHash;
        private final boolean deep;

        public PropertySegment(String propertyName, boolean deep){
            this.propertyName = propertyName;
            this.propertyNameHash = TypeUtils.fnv1a_64(propertyName);
            this.deep = deep;
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            if (deep) {
                List<Object> results = new ArrayList<Object>();
                path.deepScan(currentObject, propertyName, results);
                return results;
            } else {
                // return path.getPropertyValue(currentObject, propertyName, true);
                return path.getPropertyValue(currentObject, propertyName, propertyNameHash);
            }
        }

        public void extract(JSONPath path, DefaultJSONParser parser, Context context) {
            JSONLexerBase lexer = (JSONLexerBase) parser.lexer;

            if (deep && context.object == null) {
                context.object = new JSONArray();
            }

            if (lexer.token() == JSONToken.LBRACKET) {
                if ("*".equals(propertyName)) {
                    return;
                }

                lexer.nextToken();
                JSONArray array;

                if (deep) {
                    array =(JSONArray) context.object;
                } else {
                    array = new JSONArray();
                }
                for (;;) {
                    switch (lexer.token()) {
                        case JSONToken.LBRACE: {
                            if (deep) {
                                extract(path, parser, context);
                                break;
                            }
                            int matchStat = lexer.seekObjectToField(propertyNameHash, deep);
                            if (matchStat == JSONLexer.VALUE) {
                                Object value;
                                switch (lexer.token()) {
                                    case JSONToken.LITERAL_INT:
                                        value = lexer.integerValue();
                                        lexer.nextToken();
                                        break;
                                    case JSONToken.LITERAL_STRING:
                                        value = lexer.stringVal();
                                        lexer.nextToken();
                                        break;
                                    default:
                                        value = parser.parse();
                                        break;
                                }

                                array.add(value);
                                if (lexer.token() == JSONToken.RBRACE) {
                                    lexer.nextToken();
                                    continue;
                                } else {
                                    lexer.skipObject(false);
                                }
                            } else if (matchStat == JSONLexer.NOT_MATCH) {
                                continue;
                            } else {
                                if (deep) {
                                    throw new UnsupportedOperationException(lexer.info());
                                } else {
                                    lexer.skipObject(false);
                                }
                            }
                            break;
                        }
                        case JSONToken.LBRACKET:
                            if (deep) {
                                extract(path, parser, context);
                            } else {
                                lexer.skipObject(false);
                            }
                            break;
                        case JSONToken.LITERAL_STRING:
                        case JSONToken.LITERAL_INT:
                        case JSONToken.LITERAL_FLOAT:
                        case JSONToken.LITERAL_ISO8601_DATE:
                        case JSONToken.TRUE:
                        case JSONToken.FALSE:
                        case JSONToken.NULL:
                            lexer.nextToken();
                            break;
                        default:
                            break;
                    }

                    if (lexer.token() == JSONToken.RBRACKET) {
                        lexer.nextToken();
                        break;
                    } else if (lexer.token() == JSONToken.COMMA) {
                        lexer.nextToken();
                        continue;
                    } else {
                        throw new JSONException("illegal json : " + lexer.info());
                    }
                }

                if (!deep) {
                    if (array.size() > 0) {
                        context.object = array;
                    }
                }
                return;
            }

            if (!deep) {
                int matchStat = lexer.seekObjectToField(propertyNameHash, deep);
                if (matchStat == JSONLexer.VALUE) {
                    if (context.eval) {
                        Object value;
                        switch (lexer.token()) {
                            case JSONToken.LITERAL_INT:
                                value = lexer.integerValue();
                                lexer.nextToken(JSONToken.COMMA);
                                break;
                            case JSONToken.LITERAL_FLOAT:
                                value = lexer.decimalValue();
                                lexer.nextToken(JSONToken.COMMA);
                                break;
                            case JSONToken.LITERAL_STRING:
                                value = lexer.stringVal();
                                lexer.nextToken(JSONToken.COMMA);
                                break;
                            default:
                                value = parser.parse();
                                break;
                        }

                        if (context.eval) {
                            context.object = value;
                        }
                    }
                }
                return;
            }

            // deep
            for (;;) {
                int matchStat = lexer.seekObjectToField(propertyNameHash, deep);
                if (matchStat == JSONLexer.NOT_MATCH) {
                    break;
                }

                if (matchStat == JSONLexer.VALUE) {
                    if (context.eval) {
                        Object value;
                        switch (lexer.token()) {
                            case JSONToken.LITERAL_INT:
                                value = lexer.integerValue();
                                lexer.nextToken(JSONToken.COMMA);
                                break;
                            case JSONToken.LITERAL_FLOAT:
                                value = lexer.decimalValue();
                                lexer.nextToken(JSONToken.COMMA);
                                break;
                            case JSONToken.LITERAL_STRING:
                                value = lexer.stringVal();
                                lexer.nextToken(JSONToken.COMMA);
                                break;
                            default:
                                value = parser.parse();
                                break;
                        }

                        if (context.eval) {
                            if (context.object instanceof List) {
                                List list = (List) context.object;
                                if (list.size() == 0 && value instanceof List) {
                                    context.object = value;
                                } else {
                                    list.add(value);
                                }
                            } else {
                                context.object = value;
                            }
                        }
                    }
                } else if (matchStat == JSONLexer.OBJECT || matchStat == JSONLexer.ARRAY) {
                    extract(path, parser, context);
                }
            }
        }

        public void setValue(JSONPath path, Object parent, Object value) {
            if (deep) {
                path.deepSet(parent, propertyName, propertyNameHash, value);
            } else {
                path.setPropertyValue(parent, propertyName, propertyNameHash, value);
            }
        }
        
        public boolean remove(JSONPath path, Object parent) {
            return path.removePropertyValue(parent, propertyName, deep);
        }
    }

    static class MultiPropertySegment implements Segment {

        private final String[] propertyNames;
        private final long[]   propertyNamesHash;

        public MultiPropertySegment(String[] propertyNames){
            this.propertyNames = propertyNames;
            this.propertyNamesHash = new long[propertyNames.length];
            for (int i = 0; i < propertyNamesHash.length; i++) {
                propertyNamesHash[i] = TypeUtils.fnv1a_64(propertyNames[i]);
            }
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            List<Object> fieldValues = new ArrayList<Object>(propertyNames.length);

            for (int i = 0; i < propertyNames.length; i++) {
                Object fieldValue = path.getPropertyValue(currentObject, propertyNames[i], propertyNamesHash[i]);
                fieldValues.add(fieldValue);
            }

            return fieldValues;
        }

        public void extract(JSONPath path, DefaultJSONParser parser, Context context) {
            JSONLexerBase lexer = (JSONLexerBase) parser.lexer;

            JSONArray array;
            if (context.object == null) {
                context.object = array = new JSONArray();
            } else {
                array = (JSONArray) context.object;
            }
            for (int i = array.size(); i < propertyNamesHash.length; ++i) {
                array.add(null);
            }

//            if (lexer.token() == JSONToken.LBRACKET) {
//                lexer.nextToken();
//                JSONArray array;
//
//                array = new JSONArray();
//                for (;;) {
//                    if (lexer.token() == JSONToken.LBRACE) {
//                        int index = lexer.seekObjectToField(propertyNamesHash);
//                        int matchStat = lexer.matchStat;
//                        if (matchStat == JSONLexer.VALUE) {
//                            Object value;
//                            switch (lexer.token()) {
//                                case JSONToken.LITERAL_INT:
//                                    value = lexer.integerValue();
//                                    lexer.nextToken();
//                                    break;
//                                case JSONToken.LITERAL_STRING:
//                                    value = lexer.stringVal();
//                                    lexer.nextToken();
//                                    break;
//                                default:
//                                    value = parser.parse();
//                                    break;
//                            }
//
//                            array.add(index, value);
//                            if (lexer.token() == JSONToken.RBRACE) {
//                                lexer.nextToken();
//                                continue;
//                            } else {
//                                lexer.skipObject();
//                            }
//                        } else {
//                            lexer.skipObject();
//                        }
//                    }
//
//                    if (lexer.token() == JSONToken.RBRACKET) {
//                        break;
//                    } else if (lexer.token() == JSONToken.COMMA) {
//                        lexer.nextToken();
//                        continue;
//                    } else {
//                        throw new JSONException("illegal json.");
//                    }
//                }
//
//                context.object = array;
//                return;
//            }

            for_:
            for (;;) {
                int index = lexer.seekObjectToField(propertyNamesHash);
                int matchStat = lexer.matchStat;
                if (matchStat == JSONLexer.VALUE) {
                    Object value;
                    switch (lexer.token()) {
                        case JSONToken.LITERAL_INT:
                            value = lexer.integerValue();
                            lexer.nextToken(JSONToken.COMMA);
                            break;
                        case JSONToken.LITERAL_FLOAT:
                            value = lexer.decimalValue();
                            lexer.nextToken(JSONToken.COMMA);
                            break;
                        case JSONToken.LITERAL_STRING:
                            value = lexer.stringVal();
                            lexer.nextToken(JSONToken.COMMA);
                            break;
                        default:
                            value = parser.parse();
                            break;
                    }

                    array.set(index, value);

                    if (lexer.token() == JSONToken.COMMA) {
                        continue for_;
                    }
                }

                break;
            }
        }
    }

    static class WildCardSegment implements Segment {
        private boolean deep;

        private WildCardSegment(boolean deep) {
            this.deep = deep;
        }

        public final static WildCardSegment instance = new WildCardSegment(false);
        public final static WildCardSegment instance_deep = new WildCardSegment(true);

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            if (!deep) {
                return path.getPropertyValues(currentObject);
            }

            List<Object> values = new ArrayList<Object>();
            path.deepGetPropertyValues(currentObject, values);
            return values;
        }

        public void extract(JSONPath path, DefaultJSONParser parser, Context context) {
            if (context.eval) {
                Object object = parser.parse();
                if (deep) {
                    List<Object> values = new ArrayList<Object>();
                    path.deepGetPropertyValues(object, values);
                    context.object = values;
                    return;
                }

                if (object instanceof JSONObject) {
                    Collection<Object> values = ((JSONObject) object).values();
                    JSONArray array = new JSONArray(values.size());
                    for (Object value : values) {
                        array.add(value);
                    }
                    context.object = array;
                    return;
                } else if (object instanceof JSONArray) {
                    context.object = object;
                    return;
                }
            }

            throw new JSONException("TODO");
        }
    }

    static class ArrayAccessSegment implements Segment {

        private final int index;

        public ArrayAccessSegment(int index){
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

        public void extract(JSONPath path, DefaultJSONParser parser, Context context) {
            JSONLexerBase lexer = (JSONLexerBase) parser.lexer;
            if (lexer.seekArrayToItem(index)
                    && context.eval)
            {
                context.object = parser.parse();
            }
        }
    }

    static class MultiIndexSegment implements Segment {

        private final int[] indexes;

        public MultiIndexSegment(int[] indexes){
            this.indexes = indexes;
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            List<Object> items = new JSONArray(indexes.length);
            for (int i = 0; i < indexes.length; ++i) {
                Object item = path.getArrayItem(currentObject, indexes[i]);
                items.add(item);
            }
            return items;
        }

        public void extract(JSONPath path, DefaultJSONParser parser, Context context) {
            if (context.eval) {
                Object object = parser.parse();
                if (object instanceof List) {
                    int[] indexes = new int[this.indexes.length];
                    System.arraycopy(this.indexes, 0, indexes, 0, indexes.length);
                    boolean noneNegative = indexes[0] >= 0;

                    List list = (List) object;
                    if (noneNegative) {
                        for (int i = list.size() - 1; i >= 0; i--) {
                            if (Arrays.binarySearch(indexes, i) < 0) {
                                list.remove(i);
                            }
                        }
                        context.object = list;
                        return;
                    }
                }
            }
            throw new UnsupportedOperationException();
        }
    }

    static class RangeSegment implements Segment {

        private final int start;
        private final int end;
        private final int step;

        public RangeSegment(int start, int end, int step){
            this.start = start;
            this.end = end;
            this.step = step;
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            int size = SizeSegment.instance.eval(path, rootObject, currentObject);
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

        public void extract(JSONPath path, DefaultJSONParser parser, Context context) {
            throw new UnsupportedOperationException();
        }
    }

    static class NotNullSegement implements Filter {

        private final String propertyName;
        private final long   propertyNameHash;


        public NotNullSegement(String propertyName){
            this.propertyName = propertyName;
            this.propertyNameHash = TypeUtils.fnv1a_64(propertyName);
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, propertyNameHash);

            return propertyValue != null;
        }
    }

    static class NullSegement implements Filter {

        private final String propertyName;
        private final long   propertyNameHash;

        public NullSegement(String propertyName){
            this.propertyName = propertyName;
            this.propertyNameHash = TypeUtils.fnv1a_64(propertyName);
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, propertyNameHash);

            return propertyValue == null;
        }
    }
    
    static class ValueSegment implements Filter {
        private final String propertyName;
        private final long   propertyNameHash;
        private final Object value;
        private boolean eq = true;
        
        public ValueSegment(String propertyName, Object value, boolean eq){
            if (value == null) {
                throw new IllegalArgumentException("value is null");
            }
            this.propertyName = propertyName;
            this.propertyNameHash = TypeUtils.fnv1a_64(propertyName);
            this.value = value;
            this.eq = eq;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, propertyNameHash);
            boolean result = value.equals(propertyValue);
            if (!eq) {
                result = !result;
            }
            return result;
        }
    }

    static class IntInSegement implements Filter {

        private final String  propertyName;
        private final long    propertyNameHash;
        private final long[]  values;
        private final boolean not;

        public IntInSegement(String propertyName, long[] values, boolean not){
            this.propertyName = propertyName;
            this.propertyNameHash = TypeUtils.fnv1a_64(propertyName);
            this.values = values;
            this.not = not;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, propertyNameHash);

            if (propertyValue == null) {
                return false;
            }

            if (propertyValue instanceof Number) {
                long longPropertyValue = TypeUtils.longExtractValue((Number) propertyValue);
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
        private final long    propertyNameHash;
        private final long    startValue;
        private final long    endValue;
        private final boolean not;

        public IntBetweenSegement(String propertyName, long startValue, long endValue, boolean not){
            this.propertyName = propertyName;
            this.propertyNameHash = TypeUtils.fnv1a_64(propertyName);
            this.startValue = startValue;
            this.endValue = endValue;
            this.not = not;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, propertyNameHash);

            if (propertyValue == null) {
                return false;
            }

            if (propertyValue instanceof Number) {
                long longPropertyValue = TypeUtils.longExtractValue((Number) propertyValue);
                if (longPropertyValue >= startValue && longPropertyValue <= endValue) {
                    return !not;
                }
            }

            return not;
        }
    }

    static class IntObjInSegement implements Filter {

        private final String  propertyName;
        private final long    propertyNameHash;
        private final Long[]  values;
        private final boolean not;

        public IntObjInSegement(String propertyName, Long[] values, boolean not){
            this.propertyName = propertyName;
            this.propertyNameHash = TypeUtils.fnv1a_64(propertyName);
            this.values = values;
            this.not = not;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, propertyNameHash);

            if (propertyValue == null) {
                for (Long value : values) {
                    if (value == null) {
                        return !not;
                    }
                }

                return not;
            }

            if (propertyValue instanceof Number) {
                long longPropertyValue = TypeUtils.longExtractValue((Number) propertyValue);
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
        private final long     propertyNameHash;
        private final String[] values;
        private final boolean  not;

        public StringInSegement(String propertyName, String[] values, boolean not){
            this.propertyName = propertyName;
            this.propertyNameHash = TypeUtils.fnv1a_64(propertyName);
            this.values = values;
            this.not = not;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, propertyNameHash);

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
        private final long     propertyNameHash;
        private final long     value;
        private final Operator op;

        private BigDecimal     valueDecimal;
        private Float          valueFloat;
        private Double         valueDouble;

        public IntOpSegement(String propertyName, long value, Operator op){
            this.propertyName = propertyName;
            this.propertyNameHash = TypeUtils.fnv1a_64(propertyName);
            this.value = value;
            this.op = op;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, propertyNameHash);

            if (propertyValue == null) {
                return false;
            }

            if (!(propertyValue instanceof Number)) {
                return false;
            }

            if (propertyValue instanceof BigDecimal) {
                if (valueDecimal == null) {
                    valueDecimal = BigDecimal.valueOf(value);
                }

                int result = valueDecimal.compareTo((BigDecimal) propertyValue);
                switch (op) {
                    case EQ:
                        return result == 0;
                    case NE:
                        return result != 0;
                    case GE:
                        return 0 >= result;
                    case GT:
                        return 0 > result;
                    case LE:
                        return 0 <= result;
                    case LT:
                        return 0 < result;
                }

                return false;
            }

            if (propertyValue instanceof Float) {
                if (valueFloat == null) {
                    valueFloat = Float.valueOf(value);
                }

                int result = valueFloat.compareTo((Float) propertyValue);
                switch (op) {
                    case EQ:
                        return result == 0;
                    case NE:
                        return result != 0;
                    case GE:
                        return 0 >= result;
                    case GT:
                        return 0 > result;
                    case LE:
                        return 0 <= result;
                    case LT:
                        return 0 < result;
                }

                return false;
            }

            if (propertyValue instanceof Double) {
                if (valueDouble == null) {
                    valueDouble = Double.valueOf(value);
                }

                int result = valueDouble.compareTo((Double) propertyValue);
                switch (op) {
                    case EQ:
                        return result == 0;
                    case NE:
                        return result != 0;
                    case GE:
                        return 0 >= result;
                    case GT:
                        return 0 > result;
                    case LE:
                        return 0 <= result;
                    case LT:
                        return 0 < result;
                }

                return false;
            }

            long longValue = TypeUtils.longExtractValue((Number) propertyValue);

            switch (op) {
                case EQ:
                    return longValue == value;
                case NE:
                    return longValue != value;
                case GE:
                    return longValue >= value;
                case GT:
                    return longValue > value;
                case LE:
                    return longValue <= value;
                case LT:
                    return longValue < value;
            }

            return false;
        }
    }
    
    static class DoubleOpSegement implements Filter {
        private final String   propertyName;
        private final double   value;
        private final Operator op;

        private final long     propertyNameHash;

        public DoubleOpSegement(String propertyName, double value, Operator op){
            this.propertyName = propertyName;
            this.value = value;
            this.op = op;
            propertyNameHash = TypeUtils.fnv1a_64(propertyName);
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, propertyNameHash);

            if (propertyValue == null) {
                return false;
            }

            if (!(propertyValue instanceof Number)) {
                return false;
            }

            double doubleValue = ((Number) propertyValue).doubleValue();

            switch (op) {
                case EQ:
                    return doubleValue == value;
                case NE:
                    return doubleValue != value;
                case GE:
                    return doubleValue >= value;
                case GT:
                    return doubleValue > value;
                case LE:
                    return doubleValue <= value;
                case LT:
                    return doubleValue < value;
            }

            return false;
        }
    }

    static class RefOpSegement implements Filter {
        private final String    propertyName;
        private final Segment  refSgement;
        private final Operator  op;
        private final long      propertyNameHash;

        public RefOpSegement(String propertyName, Segment refSgement, Operator op){
            this.propertyName = propertyName;
            this.refSgement = refSgement;
            this.op = op;
            propertyNameHash = TypeUtils.fnv1a_64(propertyName);
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, propertyNameHash);

            if (propertyValue == null) {
                return false;
            }

            if (!(propertyValue instanceof Number)) {
                return false;
            }

            Object refValue = refSgement.eval(path, rootObject, rootObject);

            if (refValue instanceof Integer || refValue instanceof Long || refValue instanceof Short || refValue instanceof Byte) {
                long value = TypeUtils.longExtractValue((Number) refValue);

                if (propertyValue instanceof Integer || propertyValue instanceof Long || propertyValue instanceof Short || propertyValue instanceof Byte) {
                    long longValue = TypeUtils.longExtractValue((Number) propertyValue);

                    switch (op) {
                        case EQ:
                            return longValue == value;
                        case NE:
                            return longValue != value;
                        case GE:
                            return longValue >= value;
                        case GT:
                            return longValue > value;
                        case LE:
                            return longValue <= value;
                        case LT:
                            return longValue < value;
                    }
                } else if (propertyValue instanceof BigDecimal) {
                    BigDecimal valueDecimal = BigDecimal.valueOf(value);

                    int result = valueDecimal.compareTo((BigDecimal) propertyValue);
                    switch (op) {
                        case EQ:
                            return result == 0;
                        case NE:
                            return result != 0;
                        case GE:
                            return 0 >= result;
                        case GT:
                            return 0 > result;
                        case LE:
                            return 0 <= result;
                        case LT:
                            return 0 < result;
                    }

                    return false;
                }
            }

            throw new UnsupportedOperationException();
        }
    }

    static class MatchSegement implements Filter {

        private final String   propertyName;
        private final long     propertyNameHash;
        private final String   startsWithValue;
        private final String   endsWithValue;
        private final String[] containsValues;
        private final int      minLength;
        private final boolean  not;

        public MatchSegement(
                String propertyName,
                String startsWithValue,
                String endsWithValue,
                String[] containsValues,
                boolean not)
        {
            this.propertyName = propertyName;
            this.propertyNameHash = TypeUtils.fnv1a_64(propertyName);
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
            Object propertyValue = path.getPropertyValue(item, propertyName, propertyNameHash);

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
        private final long   propertyNameHash;
        private final Pattern pattern;
        private final boolean not;

        public RlikeSegement(String propertyName, String pattern, boolean not){
            this.propertyName = propertyName;
            this.propertyNameHash = TypeUtils.fnv1a_64(propertyName);
            this.pattern = Pattern.compile(pattern);
            this.not = not;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, propertyNameHash);

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
        private final long     propertyNameHash;
        private final String   value;
        private final Operator op;

        public StringOpSegement(String propertyName, String value, Operator op){
            this.propertyName = propertyName;
            this.propertyNameHash = TypeUtils.fnv1a_64(propertyName);
            this.value = value;
            this.op = op;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, propertyNameHash);

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

    static class RegMatchSegement implements Filter {

        private final String   propertyName;
        private final long     propertyNameHash;
        private final Pattern  pattern;
        private final Operator op;

        public RegMatchSegement(String propertyName, Pattern pattern, Operator op){
            this.propertyName = propertyName;
            this.propertyNameHash = TypeUtils.fnv1a_64(propertyName);
            this.pattern = pattern;
            this.op = op;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, propertyName, propertyNameHash);
            if (propertyValue == null) {
                return false;
            }

            String str = propertyValue.toString();

            Matcher m = pattern.matcher(str);
            return m.matches();
        }
    }

    enum Operator {
                   EQ, NE, GT, GE, LT, LE, LIKE, NOT_LIKE, RLIKE, NOT_RLIKE, IN, NOT_IN, BETWEEN, NOT_BETWEEN, And, Or, REG_MATCH
    }

    static public class FilterSegment implements Segment {

        private final Filter filter;

        public FilterSegment(Filter filter){
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

        public void extract(JSONPath path, DefaultJSONParser parser, Context context) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(JSONPath path, Object rootObject, Object currentObject) {
            if (currentObject == null) {
                return false;
            }

            if (currentObject instanceof Iterable) {
                Iterator it = ((Iterable) currentObject).iterator();
                while (it.hasNext()) {
                    Object item = it.next();

                    if (filter.apply(path, rootObject, currentObject, item)) {
                        it.remove();
                    }
                }

                return true;
            }

            return false;
        }
    }

    interface Filter {

        boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item);
    }

    static class FilterGroup implements Filter {
        private boolean and;
        private List<Filter> fitlers;

        public FilterGroup(Filter left, Filter right, boolean and) {
            fitlers = new ArrayList<Filter>(2);
            fitlers.add(left);
            fitlers.add(right);
            this.and = and;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            if (and) {
                for (Filter fitler : this.fitlers) {
                    if (!fitler.apply(path, rootObject, currentObject, item)) {
                        return false;
                    }
                }
                return true;
            } else {
                for (Filter fitler : this.fitlers) {
                    if (fitler.apply(path, rootObject, currentObject, item)) {
                        return true;
                    }
                }
                return false;
            }
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

        if (currentObject instanceof Map) {
            Map map = (Map) currentObject;
            Object value = map.get(index);
            if (value == null) {
                value = map.get(Integer.toString(index));
            }
            return value;
        }

        if (currentObject instanceof Collection) {
            Collection collection = (Collection) currentObject;
            int i = 0;
            for (Object item : collection) {
                if (i == index) {
                    return item;
                }
                i++;
            }
            return null;
        }

        if (index == 0) {
            return currentObject;
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
        if (currentObject == null) {
            return null;
        }

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

        if (currentObject instanceof Collection) {
            return (Collection) currentObject;
        }

        throw new UnsupportedOperationException();
    }

    protected void deepGetPropertyValues(final Object currentObject, List<Object> outValues) {
        final Class<?> currentClass = currentObject.getClass();

        JavaBeanSerializer beanSerializer = getJavaBeanSerializer(currentClass);

        Collection collection = null;
        if (beanSerializer != null) {
            try {
                collection = beanSerializer.getFieldValues(currentObject);
            } catch (Exception e) {
                throw new JSONPathException("jsonpath error, path " + path, e);
            }
        } else if (currentObject instanceof Map) {
            Map map = (Map) currentObject;
            collection = map.values();
        } else if (currentObject instanceof Collection) {
            collection = (Collection) currentObject;
        }

        if (collection != null) {
            for (Object fieldValue : collection) {
                if (fieldValue == null || ParserConfig.isPrimitive2(fieldValue.getClass())) {
                    outValues.add(fieldValue);
                } else {
                    deepGetPropertyValues(fieldValue, outValues);
                }
            }
            return;
        }

        throw new UnsupportedOperationException(currentClass.getName());
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
                return decimalA.equals(BigDecimal.valueOf(TypeUtils.longExtractValue(b)));
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
                BigInteger bigIntB = BigInteger.valueOf(TypeUtils.longExtractValue(b));
                
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

    final static long SIZE = 0x4dea9618e618ae3cL; // TypeUtils.fnv1a_64("size");
    final static long LENGTH = 0xea11573f1af59eb5L; // TypeUtils.fnv1a_64("length");

    protected Object getPropertyValue(Object currentObject, String propertyName, long propertyNameHash) {
        if (currentObject == null) {
            return null;
        }

        if (currentObject instanceof String) {
            try {
                JSONObject object = JSON.parseObject((String) currentObject);
                currentObject = object;
            } catch (Exception ex) {
                // skip
            }
        }

        if (currentObject instanceof Map) {
            Map map = (Map) currentObject;
            Object val = map.get(propertyName);

            if (val == null && (SIZE == propertyNameHash || LENGTH == propertyNameHash)) {
                val = map.size();
            }

            return val;
        }

        final Class<?> currentClass = currentObject.getClass();

        JavaBeanSerializer beanSerializer = getJavaBeanSerializer(currentClass);
        if (beanSerializer != null) {
            try {
                return beanSerializer.getFieldValue(currentObject, propertyName, propertyNameHash, false);
            } catch (Exception e) {
                throw new JSONPathException("jsonpath error, path " + path + ", segement " + propertyName, e);
            }
        }

        if (currentObject instanceof List) {
            List list = (List) currentObject;

            if (SIZE == propertyNameHash || LENGTH == propertyNameHash) {
                return list.size();
            }

            List<Object> fieldValues = null;

            for (int i = 0; i < list.size(); ++i) {
                Object obj = list.get(i);

                //
                if (obj == list) {
                    if (fieldValues == null) {
                        fieldValues = new JSONArray(list.size());
                    }
                    fieldValues.add(obj);
                    continue;
                }

                Object itemValue = getPropertyValue(obj, propertyName, propertyNameHash);
                if (itemValue instanceof Collection) {
                    Collection collection = (Collection) itemValue;
                    if (fieldValues == null) {
                        fieldValues = new JSONArray(list.size());
                    }
                    fieldValues.addAll(collection);
                } else if (itemValue != null) {
                    if (fieldValues == null) {
                        fieldValues = new JSONArray(list.size());
                    }
                    fieldValues.add(itemValue);
                }
            }

            if (fieldValues == null) {
                fieldValues = Collections.emptyList();
            }

            return fieldValues;
        }

        if (currentObject instanceof Object[]) {
            Object[] array = (Object[]) currentObject;

            if (SIZE == propertyNameHash || LENGTH == propertyNameHash) {
                return array.length;
            }

            List<Object> fieldValues = new JSONArray(array.length);

            for (int i = 0; i < array.length; ++i) {
                Object obj = array[i];

                //
                if (obj == array) {
                    fieldValues.add(obj);
                    continue;
                }

                Object itemValue = getPropertyValue(obj, propertyName, propertyNameHash);
                if (itemValue instanceof Collection) {
                    Collection collection = (Collection) itemValue;
                    fieldValues.addAll(collection);
                } else if (itemValue != null) {
                    fieldValues.add(itemValue);
                }
            }

            return fieldValues;
        }

        if (currentObject instanceof Enum) {
            final long NAME = 0xc4bcadba8e631b86L; // TypeUtils.fnv1a_64("name");
            final long ORDINAL = 0xf1ebc7c20322fc22L; //TypeUtils.fnv1a_64("ordinal");

            Enum e = (Enum) currentObject;
            if (NAME == propertyNameHash) {
                return e.name();
            }

            if (ORDINAL == propertyNameHash) {
                return e.ordinal();
            }
        }

        if (currentObject instanceof Calendar) {
            final long YEAR = 0x7c64634977425edcL; //TypeUtils.fnv1a_64("year");
            final long MONTH = 0xf4bdc3936faf56a5L; //TypeUtils.fnv1a_64("month");
            final long DAY = 0xca8d3918f4578f1dL; // TypeUtils.fnv1a_64("day");
            final long HOUR = 0x407efecc7eb5764fL; //TypeUtils.fnv1a_64("hour");
            final long MINUTE = 0x5bb2f9bdf2fad1e9L; //TypeUtils.fnv1a_64("minute");
            final long SECOND = 0xa49985ef4cee20bdL; //TypeUtils.fnv1a_64("second");

            Calendar e = (Calendar) currentObject;
            if (YEAR == propertyNameHash) {
                return e.get(Calendar.YEAR);
            }
            if (MONTH == propertyNameHash) {
                return e.get(Calendar.MONTH);
            }
            if (DAY == propertyNameHash) {
                return e.get(Calendar.DAY_OF_MONTH);
            }
            if (HOUR == propertyNameHash) {
                return e.get(Calendar.HOUR_OF_DAY);
            }
            if (MINUTE == propertyNameHash) {
                return e.get(Calendar.MINUTE);
            }
            if (SECOND == propertyNameHash) {
                return e.get(Calendar.SECOND);
            }
        }

        return null;
        //throw new JSONPathException("jsonpath error, path " + path + ", segement " + propertyName);
    }
    
    @SuppressWarnings("rawtypes")
    protected void deepScan(final Object currentObject, final String propertyName, List<Object> results) {
        if (currentObject == null) {
            return;
        }

        if (currentObject instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) currentObject;

            for (Map.Entry entry : map.entrySet()) {
                Object val = entry.getValue();

                if (propertyName.equals(entry.getKey())) {
                    if (val instanceof Collection) {
                        results.addAll((Collection) val);
                    } else {
                        results.add(val);
                    }
                    continue;
                }

                if (val == null || ParserConfig.isPrimitive2(val.getClass())) {
                    continue;
                }

                deepScan(val, propertyName, results);
            }

            return;
        }

        if (currentObject instanceof Collection) {
            Iterator iterator = ((Collection) currentObject).iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                if (ParserConfig.isPrimitive2(next.getClass())) {
                    continue;
                }
                deepScan(next, propertyName, results);
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

    protected void deepSet(final Object currentObject, final String propertyName, long propertyNameHash, Object value) {
        if (currentObject == null) {
            return;
        }

        if (currentObject instanceof Map) {
            Map map = (Map) currentObject;

            if (map.containsKey(propertyName)) {
                Object val = map.get(propertyName);
                map.put(propertyName, value);
                return;
            }

            for (Object val : map.values()) {
                deepSet(val, propertyName, propertyNameHash, value);
            }
            return;
        }

        final Class<?> currentClass = currentObject.getClass();

        JavaBeanDeserializer beanDeserializer = getJavaBeanDeserializer(currentClass);
        if (beanDeserializer != null) {
            try {
                FieldDeserializer fieldDeser = beanDeserializer.getFieldDeserializer(propertyName);
                if (fieldDeser != null) {
                    fieldDeser.setValue(currentObject, value);
                    return;
                }

                JavaBeanSerializer beanSerializer = getJavaBeanSerializer(currentClass);
                List<Object> fieldValues = beanSerializer.getObjectFieldValues(currentObject);
                for (Object val : fieldValues) {
                    deepSet(val, propertyName, propertyNameHash, value);
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
                deepSet(val, propertyName, propertyNameHash, value);
            }
            return;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected boolean setPropertyValue(Object parent, String name, long propertyNameHash, Object value) {
        if (parent instanceof Map) {
            ((Map) parent).put(name, value);
            return true;
        }

        if (parent instanceof List) {
            for (Object element : (List) parent) {
                if (element == null) {
                    continue;
                }
                setPropertyValue(element, name, propertyNameHash, value);
            }
            return true;
        }

        ObjectDeserializer deserializer = parserConfig.getDeserializer(parent.getClass());

        JavaBeanDeserializer beanDeserializer = null;
        if (deserializer instanceof JavaBeanDeserializer) {
            beanDeserializer = (JavaBeanDeserializer) deserializer;
        }

        if (beanDeserializer != null) {
            FieldDeserializer fieldDeserializer = beanDeserializer.getFieldDeserializer(propertyNameHash);
            if (fieldDeserializer == null) {
                return false;
            }

            fieldDeserializer.setValue(parent, value);
            return true;
        }

        throw new UnsupportedOperationException();
    }
    
    @SuppressWarnings({"rawtypes" })
    protected boolean removePropertyValue(Object parent, String name, boolean deep) {
        if (parent instanceof Map) {
            Object origin = ((Map) parent).remove(name);
            boolean found = origin != null;

            if (deep) {
                for (Object item : ((Map) parent).values()) {
                    removePropertyValue(item, name, deep);
                }
            }

            return found;
        }

        ObjectDeserializer deserializer = parserConfig.getDeserializer(parent.getClass());

        JavaBeanDeserializer beanDeserializer = null;
        if (deserializer instanceof JavaBeanDeserializer) {
            beanDeserializer = (JavaBeanDeserializer) deserializer;
        }

        if (beanDeserializer != null) {
            FieldDeserializer fieldDeserializer = beanDeserializer.getFieldDeserializer(name);

            boolean found = false;
            if (fieldDeserializer != null) {
                fieldDeserializer.setValue(parent, null);
                found = true;
            }

            if (deep) {
                Collection<Object> propertyValues = this.getPropertyValues(parent);
                for (Object item : propertyValues) {
                    if (item == null) {
                        continue;
                    }
                    removePropertyValue(item, name, deep);
                }
            }

            return found;
        }

        if (deep) {
            return false;
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

    protected JavaBeanDeserializer getJavaBeanDeserializer(final Class<?> currentClass) {
        JavaBeanDeserializer beanDeserializer = null;
        {
            ObjectDeserializer deserializer = parserConfig.getDeserializer(currentClass);
            if (deserializer instanceof JavaBeanDeserializer) {
                beanDeserializer = (JavaBeanDeserializer) deserializer;
            }
        }
        return beanDeserializer;
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    Set<?> evalKeySet(Object currentObject) {
        if (currentObject == null) {
            return null;
        }

        if (currentObject instanceof Map) {
            // For performance reasons return keySet directly, without filtering null-value key.
            return ((Map)currentObject).keySet();
        }

        if (currentObject instanceof Collection || currentObject instanceof Object[]
            || currentObject.getClass().isArray()) {
            return null;
        }

        JavaBeanSerializer beanSerializer = getJavaBeanSerializer(currentObject.getClass());
        if (beanSerializer == null) {
            return null;
        }

        try {
            return beanSerializer.getFieldNames(currentObject);
        } catch (Exception e) {
            throw new JSONPathException("evalKeySet error : " + path, e);
        }
    }

    public String toJSONString() {
        return JSON.toJSONString(path);
    }

    public static Object reserveToArray(Object object, String... paths) {
        JSONArray reserved = new JSONArray();

        if (paths == null || paths.length == 0) {
            return reserved;
        }

        for (String item : paths) {
            JSONPath path = JSONPath.compile(item);
            path.init();

            Object value = path.eval(object);
            reserved.add(value);
        }

        return reserved;
    }

    public static Object reserveToObject(Object object, String... paths) {
        if (paths == null || paths.length == 0) {
            return object;
        }

        JSONObject reserved = new JSONObject(true);
        for (String item : paths) {
            JSONPath path = JSONPath.compile(item);
            path.init();
            Segment lastSegement = path.segments[path.segments.length - 1];
            if (lastSegement instanceof PropertySegment) {
                Object value = path.eval(object);
                if (value == null) {
                    continue;
                }
                path.set(reserved, value);
            } else {
                // skip
            }
        }

        return reserved;
    }
}
