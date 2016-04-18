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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class JSONSerializer {

    public final SerializeConfig                     config;

    public final SerializeWriter                     out;

    protected List<BeforeFilter>                     beforeFilters      = null;
    protected List<AfterFilter>                      afterFilters       = null;
    protected List<PropertyFilter>                   propertyFilters    = null;
    protected List<ValueFilter>                      valueFilters       = null;
    protected List<NameFilter>                       nameFilters        = null;
    protected List<PropertyPreFilter>                propertyPreFilters = null;

    private int                                      indentCount        = 0;

    private String                                   dateFormatPattern;
    private DateFormat                               dateFormat;

    protected IdentityHashMap<Object, SerialContext> references         = null;
    protected SerialContext                          context;

    public TimeZone                                  timeZone           = JSON.defaultTimeZone;
    public Locale                                    locale             = JSON.defaultLocale;

    public JSONSerializer(){
        this(new SerializeWriter((Writer) null, JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.EMPTY),
             SerializeConfig.globalInstance);
    }

    public JSONSerializer(SerializeWriter out){
        this(out, SerializeConfig.globalInstance);
    }

    public JSONSerializer(SerializeConfig config){
        this(new SerializeWriter((Writer) null, JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.EMPTY), config);
    }

    public JSONSerializer(SerializeWriter out, SerializeConfig config){
        this.out = out;
        this.config = config;
        this.timeZone = JSON.defaultTimeZone;
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
                dateFormat = new SimpleDateFormat(dateFormatPattern, locale);
                dateFormat.setTimeZone(timeZone);
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
        if ((out.features & SerializerFeature.DisableCircularReferenceDetect.mask) == 0) {
            this.context = new SerialContext(parent, object, fieldName, features);
            if (this.references == null) {
                this.references = new IdentityHashMap<Object, SerialContext>();
            }
            this.references.put(object, context);
        }
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

        SerialContext refContext = references.get(object);

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

    public void incrementIndent() {
        indentCount++;
    }

    public void decrementIdent() {
        indentCount--;
    }

    public void println() {
        out.write('\n');
        for (int i = 0; i < indentCount; ++i) {
            out.write('\t');
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

    public List<PropertyPreFilter> getPropertyPreFilters() {
        if (propertyPreFilters == null) {
            propertyPreFilters = new ArrayList<PropertyPreFilter>();
        }

        return propertyPreFilters;
    }

    public List<PropertyFilter> getPropertyFilters() {
        if (propertyFilters == null) {
            propertyFilters = new ArrayList<PropertyFilter>();
        }

        return propertyFilters;
    }

    public String toString() {
        return out.toString();
    }

    public void config(SerializerFeature feature, boolean state) {
        out.config(feature, state);
    }

    public static final void write(Writer out, Object object) {
        SerializeWriter writer = new SerializeWriter((Writer) null, JSON.DEFAULT_GENERATE_FEATURE,
                                                     SerializerFeature.EMPTY);
        try {
            JSONSerializer serializer = new JSONSerializer(writer, SerializeConfig.globalInstance);
            serializer.write(object);
            writer.writeTo(out);
        } catch (IOException ex) {
            throw new JSONException(ex.getMessage(), ex);
        } finally {
            writer.close();
        }
    }

    public static final void write(SerializeWriter out, Object object) {
        JSONSerializer serializer = new JSONSerializer(out, SerializeConfig.globalInstance);
        serializer.write(object);
    }

    public final void write(Object object) {
        if (object == null) {
            out.writeNull();
            return;
        }

        Class<?> clazz = object.getClass();
        ObjectSerializer writer = config.get(clazz);

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
        out.writeFieldName(key, true);
        write(value);
    }

    public final void writeWithFieldName(Object object, Object fieldName, Type fieldType, int features) {
        try {
            if (object == null) {
                out.writeNull();
                return;
            }

            Class<?> clazz = object.getClass();

            ObjectSerializer writer = config.get(clazz);

            writer.write(this, object, fieldName, fieldType);
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public final void writeWithFormat(Object object, String format) {
        if (object instanceof Date) {
            DateFormat dateFormat = this.getDateFormat();
            if (dateFormat == null) {
                dateFormat = new SimpleDateFormat(format, locale);
                dateFormat.setTimeZone(timeZone);
            }
            String text = dateFormat.format((Date) object);
            out.writeString(text);
            return;
        }
        write(object);
    }

    public final void write(String value) {
        if (value == null) {
            if ((out.features & SerializerFeature.WriteNullStringAsEmpty.mask) != 0) {
                out.writeString("");
            } else {
                out.writeNull();
            }
            return;
        }

        if ((out.features & SerializerFeature.UseSingleQuotes.mask) != 0) {
            out.writeStringWithSingleQuote(value);
        } else {
            out.writeStringWithDoubleQuote(value, (char) 0, true);
        }
    }

    public void close() {
        this.out.close();
    }

    public static Object processValue(JSONSerializer serializer, Object object, Object key, Object propertyValue) {
        List<ValueFilter> valueFilters = serializer.valueFilters;
        if (valueFilters != null) {
            if (key != null && !(key instanceof String)) {
                key = JSON.toJSONString(key);
            }
            for (ValueFilter valueFilter : valueFilters) {
                propertyValue = valueFilter.process(object, (String) key, propertyValue);
            }
        }

        return propertyValue;
    }

    public Object processKey(Object object, Object key, Object propertyValue) {
        List<NameFilter> nameFilters = this.nameFilters;
        if (nameFilters != null) {
            if (key != null && !(key instanceof String)) {
                key = JSON.toJSONString(key);
            }
            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, (String) key, propertyValue);
            }
        }

        return key;
    }

    public boolean applyName(Object object, Object key) {
        List<PropertyPreFilter> filters = this.propertyPreFilters;

        if (filters == null) {
            return true;
        }

        for (PropertyPreFilter filter : filters) {
            if (key != null && !(key instanceof String)) {
                key = JSON.toJSONString(key);
            }

            if (!filter.apply(this, object, (String) key)) {
                return false;
            }
        }

        return true;
    }

    public boolean apply(Object object, Object key, Object propertyValue) {
        List<PropertyFilter> propertyFilters = this.propertyFilters;

        if (propertyFilters == null) {
            return true;
        }

        if (key != null && !(key instanceof String)) {
            key = JSON.toJSONString(key);
        }

        for (PropertyFilter propertyFilter : propertyFilters) {
            if (!propertyFilter.apply(object, (String) key, propertyValue)) {
                return false;
            }
        }

        return true;
    }

    public SerializeWriter getWriter() {
        return out;
    }
    

    public SerialContext getContext() {
        return context;
    }
}
