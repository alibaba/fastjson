package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * A simple holder for the POJO to serialize via {@link FastJsonHttpMessageConverter} along with further
 * serialization instructions to be passed in to the converter.
 *
 * <p>
 * On the server side this wrapper is added with a {@code ResponseBodyInterceptor} after content negotiation selects the
 * converter to use but before the write.
 *
 * <p>
 * On the client side, simply wrap the POJO and pass it in to the {@code RestTemplate}.
 *
 * @author Jerry.Chen
 * @since 1.2.20
 *
 * @see JSONPObject
 */
@Deprecated
public class MappingFastJsonValue implements JSONSerializable {
    private static final String SECURITY_PREFIX = "/**/";
    private static final int BrowserSecureMask = SerializerFeature.BrowserSecure.mask;

    private Object value;
    private String jsonpFunction;

    /**
     * Create a new instance wrapping the given POJO to be serialized.
     *
     * @param value the Object to be serialized
     */
    public MappingFastJsonValue(Object value) {
        this.value = value;
    }

    /**
     * Modify the POJO to serialize.
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Return the POJO that needs to be serialized.
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Set the name of the JSONP function name.
     */
    public void setJsonpFunction(String functionName) {
        this.jsonpFunction = functionName;
    }

    /**
     * Return the configured JSONP function name.
     */
    public String getJsonpFunction() {
        return this.jsonpFunction;
    }

    public void write(JSONSerializer serializer, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter writer = serializer.out;

        if(jsonpFunction == null){
            serializer.write(value);
            return;
        }


        if ((features & BrowserSecureMask) != 0 || (writer.isEnabled(BrowserSecureMask))) {
            writer.write(SECURITY_PREFIX);
        }

        writer.write(jsonpFunction);
        writer.write('(');
        serializer.write(value);
        writer.write(')');
    }
}
