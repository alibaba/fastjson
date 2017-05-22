package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.util.FieldInfo;

public class SerializeBeanInfo {

    protected final Class<?> beanType;
    protected final String   typeName;
    protected final JSONType jsonType;

    protected final FieldInfo[] fields;
    protected final FieldInfo[] sortedFields;
    
    protected int               features;

    public SerializeBeanInfo(Class<?> beanType, //
                             JSONType jsonType, //
                             String typeName, //
                             int features,
                             FieldInfo[] fields, //
                             FieldInfo[] sortedFields
                             ){
        this.beanType = beanType;
        this.jsonType = jsonType;
        this.typeName = typeName;
        this.features = features;
        this.fields = fields;
        this.sortedFields = sortedFields;
    }

}
