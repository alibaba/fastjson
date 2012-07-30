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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONStreamAware;
import com.alibaba.fastjson.util.ServiceLoader;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.sql.Clob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public class JSONSerializer {

    private final SerializeConfig                  config;

    private final SerializeWriter                  out;

    private List<PropertyFilter>                   propertyFilters   = null;
    private List<ValueFilter>                      valueFilters      = null;
    private List<NameFilter>                       nameFilters       = null;

    private int                                    indentCount       = 0;
    private String                                 indent            = "\t";

    private String                                 dateFormatPatterm = JSON.DEFFAULT_DATE_FORMAT;
    private DateFormat                             dateFormat;

    private IdentityHashMap<Object, SerialContext> references        = null;
    private SerialContext                          context;
	private SerialLinkedContext serialLinkedContext;

	public SerialLinkedContext getSerialLinkedContext() {
		return serialLinkedContext;
	}

	public void setSerialLinkedContext(SerialLinkedContext serialLinkedContext) {
		this.serialLinkedContext = serialLinkedContext;
	}

	public JSONSerializer(){
        this(new SerializeWriter(), SerializeConfig.getGlobalInstance());
    }

    public JSONSerializer(SerializeWriter out){
        this(out, SerializeConfig.getGlobalInstance());
    }

    public JSONSerializer(SerializeConfig config){
        this(new SerializeWriter(), config);
    }

    @Deprecated
    public JSONSerializer(JSONSerializerMap mapping){
        this(new SerializeWriter(), mapping);
    }

    public JSONSerializer(SerializeWriter out, SerializeConfig config){
        this.out = out;
        this.config = config;
    }

    public String getDateFormatPattern() {
        return dateFormatPatterm;
    }

    public DateFormat getDateFormat() {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(dateFormatPatterm);
        }

        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormatPatterm = dateFormat;
        if (this.dateFormat != null) {
            this.dateFormat = null;
        }
    }

    public SerialContext getContext() {
        return context;
    }

    public void setContext(SerialContext context) {
        this.context = context;
    }

    public void setContext(SerialContext parent, Object object, Object fieldName) {
        if (isEnabled(SerializerFeature.DisableCircularReferenceDetect)) {
            return;
        }

        this.context = new SerialContext(parent, object, fieldName);
        if (references == null) {
            references = new IdentityHashMap<Object, SerialContext>();
        }
        this.references.put(object, context);
    }

    public void setContext(Object object, Object fieldName) {
        this.setContext(context, object, fieldName);
    }

    public void popContext() {
        if (context != null) {
            this.context = this.context.getParent();
        }
    }

    public void setContext(SerialContext parent, Object object) {
        if (isEnabled(SerializerFeature.DisableCircularReferenceDetect)) {
            return;
        }

        this.context = new SerialContext(parent, object, null);
        if (references == null) {
            references = new IdentityHashMap<Object, SerialContext>();
        }
        this.references.put(object, context);
    }

    public boolean isWriteClassName() {
        return isEnabled(SerializerFeature.WriteClassName);
    }

    public final boolean isWriteClassName(Type fieldType, Object obj) {
        boolean result = out.isEnabled(SerializerFeature.WriteClassName);

        if (!result) {
            return false;
        }

        if (fieldType == null) {
            if (this.isEnabled(SerializerFeature.NotWriteRootClassName)) {
                boolean isRoot = context.getParent() == null;
                if (isRoot) {
                    return false;
                }
            }
        }

        return true;
    }

    public Collection<SerialContext> getReferences() {
        if (references == null) {
            references = new IdentityHashMap<Object, SerialContext>();
        }

        return references.values();
    }

    public SerialContext getSerialContext(Object object) {
        if (references == null) {
            return null;
        }

        return references.get(object);
    }

    public boolean containsReference(Object value) {
        if (isEnabled(SerializerFeature.DisableCircularReferenceDetect)) {
            return false;
        }

        if (references == null) {
            return false;
        }

        return references.containsKey(value);
    }

    public void writeReference(Object object) {
        if (isEnabled(SerializerFeature.DisableCircularReferenceDetect)) {
            return;
        }

        SerialContext context = this.getContext();
        Object current = context.getObject();

        if (object == current) {
            out.write("{\"$ref\":\"@\"}");
            return;
        }

        SerialContext parentContext = context.getParent();

        if (parentContext != null) {
            if (object == parentContext.getObject()) {
                out.write("{\"$ref\":\"..\"}");
                return;
            }
        }

        SerialContext rootContext = context;
        for (;;) {
            if (rootContext.getParent() == null) {
                break;
            }
            rootContext = rootContext.getParent();
        }

        if (object == rootContext.getObject()) {
            out.write("{\"$ref\":\"$\"}");
            return;
        }

        SerialContext refContext = this.getSerialContext(object);

        String path = refContext.getPath();

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

    public int getIndentCount() {
        return indentCount;
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

    public List<NameFilter> getNameFilters() {
        if (nameFilters == null) {
            nameFilters = new ArrayList<NameFilter>();
        }

        return nameFilters;
    }

    public List<NameFilter> getNameFiltersDirect() {
        return nameFilters;
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

    public SerializeWriter getWriter() {
        return out;
    }

    public String toString() {
        return out.toString();
    }

    public void config(SerializerFeature feature, boolean state) {
        out.config(feature, state);
    }

    public boolean isEnabled(SerializerFeature feature) {
        return out.isEnabled(feature);
    }

    public void writeNull() {
        this.out.writeNull();
    }

    public SerializeConfig getMapping() {
        return config;
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
        writeWithFieldName(object, fieldName, null);
    }

    public final void writeWithFieldName(Object object, Object fieldName, Type fieldType) {
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
            String text = new SimpleDateFormat(format).format((Date) object);
            out.writeString(text);
            return;
        }
        write(object);
    }

    public final void write(String text) {
        StringSerializer.instance.write(this, text);
    }

    public ObjectSerializer getObjectWriter(Class<?> clazz) {
        ObjectSerializer writer = config.get(clazz);

        if (writer == null) {
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            for (AutowiredObjectSerializer autowired : ServiceLoader.load(AutowiredObjectSerializer.class, classLoader)) {
                for (Type forType : autowired.getAutowiredFor()) {
                    config.put(forType, autowired);
                }
            }

            writer = config.get(clazz);
        }

        if (writer == null) {
            if (Map.class.isAssignableFrom(clazz)) {
                config.put(clazz, MapSerializer.instance);
            } else if (List.class.isAssignableFrom(clazz)) {
                config.put(clazz, ListSerializer.instance);
            } else if (Collection.class.isAssignableFrom(clazz)) {
                config.put(clazz, CollectionSerializer.instance);
            } else if (Date.class.isAssignableFrom(clazz)) {
                config.put(clazz, DateSerializer.instance);
            } else if (JSONAware.class.isAssignableFrom(clazz)) {
                config.put(clazz, JSONAwareSerializer.instance);
            } else if (JSONStreamAware.class.isAssignableFrom(clazz)) {
                config.put(clazz, JSONStreamAwareSerializer.instance);
            } else if (clazz.isEnum() || (clazz.getSuperclass() != null && clazz.getSuperclass().isEnum())) {
                config.put(clazz, EnumSerializer.instance);
            } else if (clazz.isArray()) {
                Class<?> componentType = clazz.getComponentType();
                ObjectSerializer compObjectSerializer = getObjectWriter(componentType);
                config.put(clazz, new ArraySerializer(compObjectSerializer));
            } else if (Throwable.class.isAssignableFrom(clazz)) {
                config.put(clazz, new ExceptionSerializer(clazz));
            } else if (TimeZone.class.isAssignableFrom(clazz)) {
                config.put(clazz, TimeZoneSerializer.instance);
            } else if (Appendable.class.isAssignableFrom(clazz)) {
                config.put(clazz, AppendableSerializer.instance);
            } else if (Charset.class.isAssignableFrom(clazz)) {
                config.put(clazz, CharsetSerializer.instance);
            } else if (Enumeration.class.isAssignableFrom(clazz)) {
                config.put(clazz, EnumerationSeriliazer.instance);
            } else if (Calendar.class.isAssignableFrom(clazz)) {
                config.put(clazz, CalendarSerializer.instance);
            } else if (Clob.class.isAssignableFrom(clazz)) {
                config.put(clazz, ClobSeriliazer.instance);
            } else {
                boolean isCglibProxy = false;
                boolean isJavassistProxy = false;
                for (Class<?> item : clazz.getInterfaces()) {
                    if (item.getName().equals("net.sf.cglib.proxy.Factory")) {
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

                if (Proxy.isProxyClass(clazz)) {
                    config.put(clazz, config.createJavaBeanSerializer(clazz));
                } else {
                    config.put(clazz, config.createJavaBeanSerializer(clazz));
                }
            }

            writer = config.get(clazz);
        }
        return writer;
    }

}
