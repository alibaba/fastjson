package com.alibaba.fastjson.util;

import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.JSONPathException;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.JSONStreamAware;
import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.JSONReaderScanner;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.SymbolTable;
import com.alibaba.fastjson.parser.deserializer.AutowiredObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessable;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.AfterFilter;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.BeforeFilter;
import com.alibaba.fastjson.serializer.ContextObjectSerializer;
import com.alibaba.fastjson.serializer.ContextValueFilter;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.LabelFilter;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerialContext;
import com.alibaba.fastjson.serializer.SerializeBeanInfo;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializeFilterable;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;

public class ASMClassLoader extends ClassLoader {

    private static java.security.ProtectionDomain DOMAIN;
    
    private static Map<String, Class<?>> classMapping = new HashMap<String, Class<?>>();

    static {
        DOMAIN = (java.security.ProtectionDomain) java.security.AccessController.doPrivileged(new PrivilegedAction<Object>() {

            public Object run() {
                return ASMClassLoader.class.getProtectionDomain();
            }
        });
        
        mapping(JSON.class);
        mapping(JSONObject.class);
        mapping(JSONArray.class);
        mapping(JSONPath.class);
        mapping(JSONAware.class);
        mapping(JSONException.class);
        mapping(JSONPathException.class);
        mapping(JSONReader.class);
        mapping(JSONStreamAware.class);
        mapping(JSONWriter.class);
        mapping(TypeReference.class);
        
        mapping(FieldInfo.class);
        mapping(TypeUtils.class);
        mapping(IOUtils.class);
        mapping(IdentityHashMap.class);
        mapping(ParameterizedTypeImpl.class);
        mapping(JavaBeanInfo.class);
        
        mapping(ObjectSerializer.class);
        mapping(JavaBeanSerializer.class);
        mapping(SerializeFilterable.class);
        mapping(SerializeBeanInfo.class);
        mapping(JSONSerializer.class);
        mapping(SerializeWriter.class);
        mapping(SerializeFilter.class);
        mapping(LabelFilter.class);
        mapping(ContextValueFilter.class);
        mapping(AfterFilter.class);
        mapping(BeforeFilter.class);
        mapping(NameFilter.class);
        mapping(PropertyFilter.class);
        mapping(PropertyPreFilter.class);
        mapping(ValueFilter.class);
        mapping(SerializerFeature.class);
        mapping(ContextObjectSerializer.class);
        mapping(SerialContext.class);
        mapping(SerializeConfig.class);
        
        mapping(JavaBeanDeserializer.class);
        mapping(ParserConfig.class);
        mapping(DefaultJSONParser.class);
        mapping(JSONLexer.class);
        mapping(JSONLexerBase.class);
        mapping(ParseContext.class);
        mapping(JSONToken.class);
        mapping(SymbolTable.class);
        mapping(Feature.class);
        mapping(JSONScanner.class);
        mapping(JSONReaderScanner.class);
        
        mapping(AutowiredObjectDeserializer.class);
        mapping(ObjectDeserializer.class);
        mapping(ExtraProcessor.class);
        mapping(ExtraProcessable.class);
        mapping(ExtraTypeProvider.class);
        mapping(BeanContext.class);
        mapping(FieldDeserializer.class);
        mapping(DefaultFieldDeserializer.class);
    }
    
    private static void mapping(Class<?> clazz) {
        classMapping.put(clazz.getName(), clazz);
    }

    public ASMClassLoader(){
        super(getParentClassLoader());
    }

    public ASMClassLoader(ClassLoader parent){
        super (parent);
    }

    static ClassLoader getParentClassLoader() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            try {
                contextClassLoader.loadClass(JSON.class.getName());
                return contextClassLoader;
            } catch (ClassNotFoundException e) {
                // skip
            }
        }
        return JSON.class.getClassLoader();
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> mappingClass = classMapping.get(name);
        if (mappingClass != null) {
            return mappingClass;
        }
        
        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException e) {
            throw e;
        }
    }

    public Class<?> defineClassPublic(String name, byte[] b, int off, int len) throws ClassFormatError {
        Class<?> clazz = defineClass(name, b, off, len, DOMAIN);

        return clazz;
    }

    public boolean isExternalClass(Class<?> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();

        if (classLoader == null) {
            return false;
        }

        ClassLoader current = this;
        while (current != null) {
            if (current == classLoader) {
                return false;
            }

            current = current.getParent();
        }

        return true;
    }

}
