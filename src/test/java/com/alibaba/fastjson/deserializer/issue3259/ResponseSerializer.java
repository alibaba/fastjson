package com.alibaba.fastjson.deserializer.issue3259;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;


public class ResponseSerializer implements ObjectSerializer, BaseSerializable {

    public static final ResponseSerializer instance = new ResponseSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {

        SerializeWriter out = serializer.getWriter();

        out.append("{");
        Response resMsg = (Response) object;


        out.writeFieldName(RES_CLA);
        out.writeString(resMsg.getResponse().getClass().getName());

        out.append(",");
        out.writeFieldName(RES);
        ObjectSerializer responseSerializer = serializer.getObjectWriter(resMsg.getResponse().getClass());
        responseSerializer.write(serializer, resMsg.getResponse(), RES, null, features);

        out.append("}");

    }
}
