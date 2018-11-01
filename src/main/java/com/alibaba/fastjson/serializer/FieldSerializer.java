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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class FieldSerializer implements Comparable<FieldSerializer> {

    public final FieldInfo        fieldInfo;
    protected final boolean       writeNull;
    protected int                 features;

    private final String          double_quoted_fieldPrefix;
    private String                single_quoted_fieldPrefix;
    private String                un_quoted_fieldPrefix;

    protected BeanContext         fieldContext;

    private String                format;
    protected boolean             writeEnumUsingToString  = false;
    protected boolean             writeEnumUsingName      = false;
    protected boolean             disableCircularReferenceDetect = false;

    protected boolean             serializeUsing          = false;

    protected boolean             persistenceXToMany      = false; // OneToMany or ManyToMany
    protected boolean             browserCompatible;

    private RuntimeSerializerInfo runtimeInfo;
    
    public FieldSerializer(Class<?> beanType, FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
        this.fieldContext = new BeanContext(beanType, fieldInfo);

        if (beanType != null && (fieldInfo.isEnum || fieldInfo.fieldClass == long.class || fieldInfo.fieldClass == Long.class)) {
            JSONType jsonType = TypeUtils.getAnnotation(beanType,JSONType.class);
            if (jsonType != null) {
                for (SerializerFeature feature : jsonType.serialzeFeatures()) {
                    if (feature == SerializerFeature.WriteEnumUsingToString) {
                        writeEnumUsingToString = true;
                    } else if(feature == SerializerFeature.WriteEnumUsingName){
                        writeEnumUsingName = true;
                    } else if(feature == SerializerFeature.DisableCircularReferenceDetect){
                        disableCircularReferenceDetect = true;
                    } else if(feature == SerializerFeature.BrowserCompatible){
                        browserCompatible = true;
                    }
                }
            }
        }
        
        fieldInfo.setAccessible();

        this.double_quoted_fieldPrefix = '"' + fieldInfo.name + "\":";

        boolean writeNull = false;
        JSONField annotation = fieldInfo.getAnnotation();
        if (annotation != null) {
            for (SerializerFeature feature : annotation.serialzeFeatures()) {
                if ((feature.getMask() & SerializerFeature.WRITE_MAP_NULL_FEATURES) != 0) {
                    writeNull = true;
                    break;
                }
            }

            format = annotation.format();

            if (format.trim().length() == 0) {
                format = null;
            }

            for (SerializerFeature feature : annotation.serialzeFeatures()) {
                if (feature == SerializerFeature.WriteEnumUsingToString) {
                    writeEnumUsingToString = true;
                } else if(feature == SerializerFeature.WriteEnumUsingName){
                    writeEnumUsingName = true;
                } else if(feature == SerializerFeature.DisableCircularReferenceDetect){
                    disableCircularReferenceDetect = true;
                } else if(feature == SerializerFeature.BrowserCompatible){
                    browserCompatible = true;
                }
            }
            
            features = SerializerFeature.of(annotation.serialzeFeatures());
        }
        
        this.writeNull = writeNull;

        persistenceXToMany = TypeUtils.isAnnotationPresentOneToMany(fieldInfo.method)
                || TypeUtils.isAnnotationPresentManyToMany(fieldInfo.method);
    }

    public void writePrefix(JSONSerializer serializer) throws IOException {
        SerializeWriter out = serializer.out;

        if (out.quoteFieldNames) {
            if (out.useSingleQuotes) {
                if (single_quoted_fieldPrefix == null) {
                    single_quoted_fieldPrefix = '\'' + fieldInfo.name + "\':";
                }
                out.write(single_quoted_fieldPrefix);
            } else {
                out.write(double_quoted_fieldPrefix);
            }
        } else {
            if (un_quoted_fieldPrefix == null) {
                this.un_quoted_fieldPrefix = fieldInfo.name + ":";
            }
            out.write(un_quoted_fieldPrefix);
        }
    }

    public Object getPropertyValueDirect(Object object) throws InvocationTargetException, IllegalAccessException {
        Object fieldValue =  fieldInfo.get(object);
        if (persistenceXToMany && !TypeUtils.isHibernateInitialized(fieldValue)) {
            return null;
        }
        return fieldValue;
    }

    public Object getPropertyValue(Object object) throws InvocationTargetException, IllegalAccessException {
        Object propertyValue =  fieldInfo.get(object);
        if (format != null && propertyValue != null) {
            if (fieldInfo.fieldClass == Date.class) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format, JSON.defaultLocale);
                dateFormat.setTimeZone(JSON.defaultTimeZone);
                return dateFormat.format(propertyValue);
            }
        }
        return propertyValue;
    }
    
    public int compareTo(FieldSerializer o) {
        return this.fieldInfo.compareTo(o.fieldInfo);
    }
    

    public void writeValue(JSONSerializer serializer, Object propertyValue) throws Exception {
        if (runtimeInfo == null) {

            Class<?> runtimeFieldClass;
            if (propertyValue == null) {
                runtimeFieldClass = this.fieldInfo.fieldClass;
                if (runtimeFieldClass == byte.class) {
                    runtimeFieldClass = Byte.class;
                } else if (runtimeFieldClass == short.class) {
                    runtimeFieldClass = Short.class;
                } else if (runtimeFieldClass == int.class) {
                    runtimeFieldClass = Integer.class;
                } else if (runtimeFieldClass == long.class) {
                    runtimeFieldClass = Long.class;
                } else if (runtimeFieldClass == float.class) {
                    runtimeFieldClass = Float.class;
                } else if (runtimeFieldClass == double.class) {
                    runtimeFieldClass = Double.class;
                } else if (runtimeFieldClass == boolean.class) {
                    runtimeFieldClass = Boolean.class;
                }
            } else {
                runtimeFieldClass = propertyValue.getClass();
            }

            ObjectSerializer fieldSerializer = null;
            JSONField fieldAnnotation = fieldInfo.getAnnotation();

            if (fieldAnnotation != null && fieldAnnotation.serializeUsing() != Void.class) {
                fieldSerializer = (ObjectSerializer) fieldAnnotation.serializeUsing().newInstance();
                serializeUsing = true;
            } else {
                if (format != null) {
                    if (runtimeFieldClass == double.class || runtimeFieldClass == Double.class) {
                        fieldSerializer = new DoubleSerializer(format);
                    } else if (runtimeFieldClass == float.class || runtimeFieldClass == Float.class) {
                        fieldSerializer = new FloatCodec(format);
                    }
                }

                if (fieldSerializer == null) {
                    fieldSerializer = serializer.getObjectWriter(runtimeFieldClass);
                }
            }

            runtimeInfo = new RuntimeSerializerInfo(fieldSerializer, runtimeFieldClass);
        }
        
        final RuntimeSerializerInfo runtimeInfo = this.runtimeInfo;
        
        final int fieldFeatures = disableCircularReferenceDetect?
                (fieldInfo.serialzeFeatures|SerializerFeature.DisableCircularReferenceDetect.getMask()):fieldInfo.serialzeFeatures;

        if (propertyValue == null) {
            SerializeWriter out  = serializer.out;

            if (fieldInfo.fieldClass == Object.class
                    && out.isEnabled(SerializerFeature.WRITE_MAP_NULL_FEATURES)) {
                out.writeNull();
                return;
            }

            Class<?> runtimeFieldClass = runtimeInfo.runtimeFieldClass;

            if (Number.class.isAssignableFrom(runtimeFieldClass)) {
                out.writeNull(features, SerializerFeature.WriteNullNumberAsZero.mask);
                return;
            } else if (String.class == runtimeFieldClass) {
                out.writeNull(features, SerializerFeature.WriteNullStringAsEmpty.mask);
                return;
            } else if (Boolean.class == runtimeFieldClass) {
                out.writeNull(features, SerializerFeature.WriteNullBooleanAsFalse.mask);
                return;
            } else if (Collection.class.isAssignableFrom(runtimeFieldClass)) {
                out.writeNull(features, SerializerFeature.WriteNullListAsEmpty.mask);
                return;
            }

            ObjectSerializer fieldSerializer = runtimeInfo.fieldSerializer;

            if ((out.isEnabled(SerializerFeature.WRITE_MAP_NULL_FEATURES))
                    && fieldSerializer instanceof JavaBeanSerializer) {
                out.writeNull();
                return;
            }

            fieldSerializer.write(serializer, null, fieldInfo.name, fieldInfo.fieldType, fieldFeatures);
            return;
        }

        if (fieldInfo.isEnum) {
            if (writeEnumUsingName) {
                serializer.out.writeString(((Enum<?>) propertyValue).name());
                return;
            }

            if (writeEnumUsingToString) {
                serializer.out.writeString(((Enum<?>) propertyValue).toString());
                return;
            }
        }
        
        Class<?> valueClass = propertyValue.getClass();
        ObjectSerializer valueSerializer;
        if (valueClass == runtimeInfo.runtimeFieldClass || serializeUsing) {
            valueSerializer = runtimeInfo.fieldSerializer;
        } else {
            valueSerializer = serializer.getObjectWriter(valueClass);
        }
        
        if (format != null && !(valueSerializer instanceof DoubleSerializer || valueSerializer instanceof FloatCodec)) {
            if (valueSerializer instanceof ContextObjectSerializer) {
                ((ContextObjectSerializer) valueSerializer).write(serializer, propertyValue, this.fieldContext);    
            } else {
                serializer.writeWithFormat(propertyValue, format);
            }
            return;
        }

        if (fieldInfo.unwrapped) {
            if (valueSerializer instanceof JavaBeanSerializer) {
                JavaBeanSerializer javaBeanSerializer = (JavaBeanSerializer) valueSerializer;
                javaBeanSerializer.write(serializer, propertyValue, fieldInfo.name, fieldInfo.fieldType, fieldFeatures, true);
                return;
            }

            if (valueSerializer instanceof MapSerializer) {
                MapSerializer mapSerializer = (MapSerializer) valueSerializer;
                mapSerializer.write(serializer, propertyValue, fieldInfo.name, fieldInfo.fieldType, fieldFeatures, true);
                return;
            }
        }

        if ((features & SerializerFeature.WriteClassName.mask) != 0
                && valueClass != fieldInfo.fieldClass
                && JavaBeanSerializer.class.isInstance(valueSerializer)) {
            ((JavaBeanSerializer) valueSerializer).write(serializer, propertyValue, fieldInfo.name, fieldInfo.fieldType, fieldFeatures, false);
            return;
        }

        if (browserCompatible && propertyValue != null
                && (fieldInfo.fieldClass == long.class || fieldInfo.fieldClass == Long.class)) {
            long value = (Long) propertyValue;
            if (value > 9007199254740991L || value < -9007199254740991L) {
                serializer.getWriter().writeString(Long.toString(value));
                return;
            }
        }

        valueSerializer.write(serializer, propertyValue, fieldInfo.name, fieldInfo.fieldType, fieldFeatures);
    }

    static class RuntimeSerializerInfo {
        final ObjectSerializer fieldSerializer;
        final Class<?>         runtimeFieldClass;

        public RuntimeSerializerInfo(ObjectSerializer fieldSerializer, Class<?> runtimeFieldClass){
            this.fieldSerializer = fieldSerializer;
            this.runtimeFieldClass = runtimeFieldClass;
        }
    }
}
