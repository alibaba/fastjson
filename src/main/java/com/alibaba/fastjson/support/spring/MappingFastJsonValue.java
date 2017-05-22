package com.alibaba.fastjson.support.spring;

/**
 * A simple holder for the POJO to serialize via {@link FastJsonpHttpMessageConverter4} along with further
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
 */
public class MappingFastJsonValue {
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
}
