/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONStreamAware;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class JSONSerializer {

    public final SerializeConfig                  config;

    public final SerializeWriter                   out;

    protected List<BeforeFilter>                   beforeFilters      = null;
    protected List<AfterFilter>                    afterFilters       = null;
    protected List<PropertyFilter>                 propertyFilters    = null;
    protected List<ValueFilter>                    valueFilters       = null;
    protected List<NameFilter>                     nameFilters        = null;
    protected List<PropertyPreFilter>              propertyPreFilters = null;

    private int                                    indentCount        = 0;
    private String                                 indent             = "\t";

    private String                                 dateFormatPattern;
    private DateFormat                             dateFormat;

    private IdentityHashMap<Object, SerialContext> references         = null;
    public SerialContext                           context;

    public JSONSerializer(){
        this(new SerializeWriter(), SerializeConfig.globalInstance);
    }

    public JSONSerializer(SerializeWriter out){
        this(out, SerializeConfig.globalInstance);
    }

    public JSONSerializer(SerializeConfig config){
        this(new SerializeWriter(), config);
    }

    public JSONSerializer(SerializeWriter out, SerializeConfig config){
        this.out = out;
        this.config = config;
    }

    public String getDateFormatPattern() {
        if (dateFormat instanceof SimpleDateFormat) {
            return ((SimpleDateFormat) dateFormat).toPattern();
        }
        return dateFormatPattern;
    }

    public DateFormat getDateFormat() {
        if (dateFormat == null) {
            if (dateFormatPattern != null) {
                dateFormat = new SimpleDateFormat(dateFormatPattern);
            }
        }

        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
        if (dateFormatPattern != null) {
            dateFormatPattern = null;
        }
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormatPattern = dateFormat;
        if (this.dateFormat != null) {
            this.dateFormat = null;
        }
    }

    public void setContext(SerialContext parent, Object object, Object fieldName, int features) {
        if ((out.features & SerializerFeature.DisableCircularReferenceDetect.mask) != 0) {
            return;
        }

        this.context = new SerialContext(parent, object, fieldName, features);
        if (references == null) {
            references = new IdentityHashMap<Object, SerialContext>();
        }
        this.references.put(object, context);
    }

    public final boolean isWriteClassName(Type fieldType, Object obj) {
        boolean result = (out.features & SerializerFeature.WriteClassName.mask) != 0;

        if (!result) {
            return false;
        }

        if (fieldType == null) {
            if ((out.features & SerializerFeature.NotWriteRootClassName.mask) != 0) {
                boolean isRoot = context.parent == null;
                if (isRoot) {
                    return false;
                }
            }
        }

        return true;
    }

    public SerialContext getSerialContext(Object object) {
        if (references == null) {
            return null;
        }

        return references.get(object);
    }

    public boolean containsReference(Object value) {
        if (references == null) {
            return false;
        }

        return references.containsKey(value);
    }

    public void writeReference(Object object) {
        SerialContext context = this.context;
        Object current = context.object;

        if (object == current) {
            out.write("{\"$ref\":\"@\"}");
            return;
        }

        SerialContext parentContext = context.parent;

        if (parentContext != null) {
            if (object == parentContext.object) {
                out.write("{\"$ref\":\"..\"}");
                return;
            }
        }

        SerialContext rootContext = context;
        for (;;) {
            if (rootContext.parent == null) {
                break;
            }
            rootContext = rootContext.parent;
        }

        if (object == rootContext.object) {
            out.write("{\"$ref\":\"$\"}");
            return;
        }

        SerialContext refContext = this.getSerialContext(object);

        String path = refContext.toString();

        out.write("{\"$ref\":\"");
        out.write(path);
        out.write("\"}");
        return;
    }

    public List<ValueFilter> getValueFilters() {
        if (valueFilters == null) {
            valueFilters = new ArrayList<ValueFilter>();
        }

        return valueFilters;
    }

    public List<ValueFilter> getValueFiltersDirect() {
        return valueFilters;
    }

    public void incrementIndent() {
        indentCount++;
    }

    public void decrementIdent() {
        indentCount--;
    }

    public void println() {
        out.write('\n');
        for (int i = 0; i < indentCount; ++i) {
            out.write(indent);
        }
    }

    public List<BeforeFilter> getBeforeFilters() {
        if (beforeFilters == null) {
            beforeFilters = new ArrayList<BeforeFilter>();
        }

        return beforeFilters;
    }

    public List<AfterFilter> getAfterFilters() {
        if (afterFilters == null) {
            afterFilters = new ArrayList<AfterFilter>();
        }

        return afterFilters;
    }

    public List<NameFilter> getNameFilters() {
        if (nameFilters == null) {
            nameFilters = new ArrayList<NameFilter>();
        }

        return nameFilters;
    }

    public List<NameFilter> getNameFiltersDirect() {
        return nameFilters;
    }

    public List<PropertyPreFilter> getPropertyPreFilters() {
        if (propertyPreFilters == null) {
            propertyPreFilters = new ArrayList<PropertyPreFilter>();
        }

        return propertyPreFilters;
    }

    public List<PropertyPreFilter> getPropertyPreFiltersDirect() {
        return propertyPreFilters;
    }

    public List<PropertyFilter> getPropertyFilters() {
        if (propertyFilters == null) {
            propertyFilters = new ArrayList<PropertyFilter>();
        }

        return propertyFilters;
    }

    public List<PropertyFilter> getPropertyFiltersDirect() {
        return propertyFilters;
    }

    public String toString() {
        return out.toString();
    }

    public void config(SerializerFeature feature, boolean state) {
        out.config(feature, state);
    }

    public static final void write(Writer out, Object object) {
        SerializeWriter writer = new SerializeWriter();
        try {
            JSONSerializer serializer = new JSONSerializer(writer);
            serializer.write(object);
            writer.writeTo(out);
        } catch (IOException ex) {
            throw new JSONException(ex.getMessage(), ex);
        } finally {
            writer.close();
        }
    }

    public static final void write(SerializeWriter out, Object object) {
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.write(object);
    }

    public final void write(Object object) {
        if (object == null) {
            out.writeNull();
            return;
        }

        Class<?> clazz = object.getClass();
        ObjectSerializer writer = getObjectWriter(clazz);

        try {
            writer.write(this, object, null, null);
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public final void writeWithFieldName(Object object, Object fieldName) {
        writeWithFieldName(object, fieldName, null, 0);
    }

    protected final void writeKeyValue(char seperator, String key, Object value) {
        if (seperator != '\0') {
            out.write(seperator);
        }
        out.writeFieldName(key);
        write(value);
    }

    public final void writeWithFieldName(Object object, Object fieldName, Type fieldType, int features) {
        try {
            if (object == null) {
                out.writeNull();
                return;
            }

            Class<?> clazz = object.getClass();

            ObjectSerializer writer = getObjectWriter(clazz);

            writer.write(this, object, fieldName, fieldType);
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public final void writeWithFormat(Object object, String format) {
        if (object instanceof Date) {
            DateFormat dateFormat = this.getDateFormat();
            if (dateFormat == null) {
                dateFormat = new SimpleDateFormat(format);
            }
            String text = dateFormat.format((Date) object);
            out.writeString(text);
            return;
        }
        write(object);
    }

    public final void write(String text) {
        StringCodec.instance.write(this, text);
    }

    public ObjectSerializer getObjectWriter(Class<?> clazz) {
        ObjectSerializer writer = config.get(clazz);

        if (writer == null) {
            Class<?> superClass;
            if (Map.class.isAssignableFrom(clazz)) {
                config.put(clazz, MapCodec.instance);
            } else if (List.class.isAssignableFrom(clazz)) {
                config.put(clazz, ListSerializer.instance);
            } else if (Collection.class.isAssignableFrom(clazz)) {
                config.put(clazz, CollectionCodec.instance);
            } else if (Date.class.isAssignableFrom(clazz)) {
                config.put(clazz, DateCodec.instance);
            } else if (JSONAware.class.isAssignableFrom(clazz)) {
                config.put(clazz, MiscCodec.instance);
            } else if (JSONSerializable.class.isAssignableFrom(clazz)) {
                config.put(clazz, MiscCodec.instance);
            } else if (JSONStreamAware.class.isAssignableFrom(clazz)) {
                config.put(clazz, MiscCodec.instance);
            } else if (clazz.isEnum() 
                    || ((superClass = clazz.getSuperclass()) != null && superClass != Object.class && superClass.isEnum())) {
                config.put(clazz, EnumSerializer.instance);
            } else if (clazz.isArray()) {
                Class<?> componentType = clazz.getComponentType();
                ObjectSerializer compObjectSerializer = getObjectWriter(componentType);
                config.put(clazz, new ArraySerializer(componentType, compObjectSerializer));
            } else if (Throwable.class.isAssignableFrom(clazz)) {
                JavaBeanSerializer serializer = new JavaBeanSerializer(clazz);
                serializer.features |= SerializerFeature.WriteClassName.mask;
                config.put(clazz, serializer);
            } else if (TimeZone.class.isAssignableFrom(clazz)) {
                config.put(clazz, MiscCodec.instance);
            } else if (Charset.class.isAssignableFrom(clazz)) {
                config.put(clazz, MiscCodec.instance);
            } else if (Enumeration.class.isAssignableFrom(clazz)) {
                config.put(clazz, MiscCodec.instance);
            } else if (Calendar.class.isAssignableFrom(clazz)) {
                config.put(clazz, CalendarCodec.instance);
            } else {
                boolean isCglibProxy = false;
                boolean isJavassistProxy = false;
                for (Class<?> item : clazz.getInterfaces()) {
                    if (item.getName().equals("net.sf.cglib.proxy.Factory")
                        || item.getName().equals("org.springframework.cglib.proxy.Factory")) {
                        isCglibProxy = true;
                        break;
                    } else if (item.getName().equals("javassist.util.proxy.ProxyObject")) {
                        isJavassistProxy = true;
                        break;
                    }
                }

                if (isCglibProxy || isJavassistProxy) {
                    Class<?> superClazz = clazz.getSuperclass();

                    ObjectSerializer superWriter = getObjectWriter(superClazz);
                    config.put(clazz, superWriter);
                    return superWriter;
                }

                config.put(clazz, config.createJavaBeanSerializer(clazz));
            }

            writer = config.get(clazz);
        }
        return writer;
    }

    public void close() {
        this.out.close();
    }

    public static char writeBefore(JSONSerializer serializer, Object object, char seperator) {
        List<BeforeFilter> beforeFilters = serializer.beforeFilters;
        if (beforeFilters != null) {
            for (BeforeFilter beforeFilter : beforeFilters) {
                seperator = beforeFilter.writeBefore(serializer, object, seperator);
            }
        }
        return seperator;
    }

    public static Object processValue(JSONSerializer serializer, Object object, String key, Object propertyValue) {
        List<ValueFilter> valueFilters = serializer.valueFilters;
        if (valueFilters != null) {
            for (ValueFilter valueFilter : valueFilters) {
                propertyValue = valueFilter.process(object, key, propertyValue);
            }
        }

        return propertyValue;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, Object propertyValue) {
        List<NameFilter> nameFilters = serializer.nameFilters;
        if (nameFilters != null) {
            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static boolean applyName(JSONSerializer serializer, Object object, String key) {
        List<PropertyPreFilter> filters = serializer.propertyPreFilters;

        if (filters == null) {
            return true;
        }

        for (PropertyPreFilter filter : filters) {
            if (!filter.apply(serializer, object, key)) {
                return false;
            }
        }

        return true;
    }

    public static boolean apply(JSONSerializer serializer, Object object, String key, Object propertyValue) {
        List<PropertyFilter> propertyFilters = serializer.propertyFilters;

        if (propertyFilters == null) {
            return true;
        }

        for (PropertyFilter propertyFilter : propertyFilters) {
            if (!propertyFilter.apply(object, key, propertyValue)) {
                return false;
            }
        }

        return true;
    }
    
    public static char writeAfter(JSONSerializer serializer, Object object, char seperator) {
        List<AfterFilter> afterFilters = serializer.afterFilters;
        if (afterFilters != null) {
            for (AfterFilter afterFilter : afterFilters) {
                seperator = afterFilter.writeAfter(serializer, object, seperator);
            }
        }
        return seperator;
    }
}
