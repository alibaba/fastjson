/*
 * Copyright 1999-2018 Alibaba Group.
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPOutputStream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.IOUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class JSONSerializer extends SerializeFilterable {

    protected final SerializeConfig                  config;
    public final SerializeWriter                     out;

    private int                                      indentCount = 0;
    private String                                   indent      = "\t";

    private String                                   dateFormatPattern;
    private DateFormat                               dateFormat;

    protected IdentityHashMap<Object, SerialContext> references  = null;
    protected SerialContext                          context;

    protected TimeZone                               timeZone    = JSON.defaultTimeZone;
    protected Locale                                 locale      = JSON.defaultLocale;

    public JSONSerializer(){
        this(new SerializeWriter(), SerializeConfig.getGlobalInstance());
    }

    public JSONSerializer(SerializeWriter out){
        this(out, SerializeConfig.getGlobalInstance());
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

    public SerialContext getContext() {
        return context;
    }

    public void setContext(SerialContext context) {
        this.context = context;
    }

    public void setContext(SerialContext parent, Object object, Object fieldName, int features) {
        this.setContext(parent, object, fieldName, features, 0);
    }

    public void setContext(SerialContext parent, Object object, Object fieldName, int features, int fieldFeatures) {
        if (out.disableCircularReferenceDetect) {
            return;
        }

        this.context = new SerialContext(parent, object, fieldName, features, fieldFeatures);
        if (references == null) {
            references = new IdentityHashMap<Object, SerialContext>();
        }
        this.references.put(object, context);
    }

    public void setContext(Object object, Object fieldName) {
        this.setContext(context, object, fieldName, 0);
    }

    public void popContext() {
        if (context != null) {
            this.context = this.context.parent;
        }
    }

    public final boolean isWriteClassName(Type fieldType, Object obj) {
        return out.isEnabled(SerializerFeature.WriteClassName) //
               && (fieldType != null //
                   || (!out.isEnabled(SerializerFeature.NotWriteRootClassName)) //
                   || (context != null && (context.parent != null)));
    }

    public boolean containsReference(Object value) {
        if (references == null) {
            return false;
        }

        SerialContext refContext = references.get(value);
        if (refContext == null) {
            return false;
        }

        if (value == Collections.emptyMap()) {
            return false;
        }

        Object fieldName = refContext.fieldName;

        return fieldName == null || fieldName instanceof Integer || fieldName instanceof String;
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
        } else {
            out.write("{\"$ref\":\"");
            String path = references.get(object).toString();
            out.write(path);
            out.write("\"}");
        }
    }

    public boolean checkValue(SerializeFilterable filterable) {
        return (valueFilters != null && valueFilters.size() > 0) //
               || (contextValueFilters != null && contextValueFilters.size() > 0) //
               || (filterable.valueFilters != null && filterable.valueFilters.size() > 0)
               || (filterable.contextValueFilters != null && filterable.contextValueFilters.size() > 0)
               || out.writeNonStringValueAsString;
    }
    
    public boolean hasNameFilters(SerializeFilterable filterable) {
        return (nameFilters != null && nameFilters.size() > 0) //
               || (filterable.nameFilters != null && filterable.nameFilters.size() > 0);
    }

    public boolean hasPropertyFilters(SerializeFilterable filterable) {
        return (propertyFilters != null && propertyFilters.size() > 0) //
                || (filterable.propertyFilters != null && filterable.propertyFilters.size() > 0);
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

    public static void write(Writer out, Object object) {
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

    public static void write(SerializeWriter out, Object object) {
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
            writer.write(this, object, null, null, 0);
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    /**
     * @since 1.2.57
     *
     */
    public final void writeAs(Object object, Class type) {
        if (object == null) {
            out.writeNull();
            return;
        }

        ObjectSerializer writer = getObjectWriter(type);

        try {
            writer.write(this, object, null, null, 0);
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

    public final void writeWithFieldName(Object object, Object fieldName, Type fieldType, int fieldFeatures) {
        try {
            if (object == null) {
                out.writeNull();
                return;
            }

            Class<?> clazz = object.getClass();

            ObjectSerializer writer = getObjectWriter(clazz);

            writer.write(this, object, fieldName, fieldType, fieldFeatures);
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public final void writeWithFormat(Object object, String format) {
        if (object instanceof Date) {
            if ("unixtime".equals(format)) {
                long seconds = ((Date) object).getTime() / 1000L;
                out.writeInt((int) seconds);
                return;
            }

            if ("millis".equals(format)) {
                out.writeLong(((Date) object).getTime());
                return;
            }

            DateFormat dateFormat = this.getDateFormat();
            if (dateFormat == null) {
                try {
                    dateFormat = new SimpleDateFormat(format, locale);
                } catch (IllegalArgumentException e) {
                    String format2 = format.replaceAll("T", "'T'");
                    dateFormat = new SimpleDateFormat(format2, locale);
                }
                dateFormat.setTimeZone(timeZone);
            }
            String text = dateFormat.format((Date) object);
            out.writeString(text);
            return;
        }

        if (object instanceof byte[]) {
            byte[] bytes = (byte[]) object;
            if ("gzip".equals(format) || "gzip,base64".equals(format)) {
                GZIPOutputStream gzipOut = null;
                try {
                    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                    if (bytes.length < 512) {
                        gzipOut = new GZIPOutputStream(byteOut, bytes.length);
                    } else {
                        gzipOut = new GZIPOutputStream(byteOut);
                    }
                    gzipOut.write(bytes);
                    gzipOut.finish();
                    out.writeByteArray(byteOut.toByteArray());
                } catch (IOException ex) {
                    throw new JSONException("write gzipBytes error", ex);
                } finally {
                    IOUtils.close(gzipOut);
                }
            } else if ("hex".equals(format)) {
                out.writeHex(bytes);
            } else {
                out.writeByteArray(bytes);
            }
            return;
        }

        if (object instanceof Collection) {
            Collection collection = (Collection) object;
            Iterator iterator = collection.iterator();
            out.write('[');
            for (int i = 0; i < collection.size(); i++) {
                Object item = iterator.next();
                if (i != 0) {
                    out.write(',');
                }
                writeWithFormat(item, format);
            }
            out.write(']');
            return;
        }
        write(object);
    }

    public final void write(String text) {
        StringCodec.instance.write(this, text);
    }

    public ObjectSerializer getObjectWriter(Class<?> clazz) {
        return config.getObjectWriter(clazz);
    }

    public void close() {
        this.out.close();
    }
   
}
