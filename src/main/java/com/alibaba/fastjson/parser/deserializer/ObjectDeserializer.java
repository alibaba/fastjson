package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

/**
 * <p>Interface representing a custom deserializer for Json. You should write a custom
 * deserializer, if you are not happy with the default deserialization done by Gson. You will
 * also need to register this deserializer through
 * {@link ParserConfig#putDeserializer(Type, ObjectDeserializer)}.</p>
 * <pre>
 * public static enum OrderActionEnum {
 *     FAIL(1), SUCC(0);
 * 
 *     private int code;
 * 
 *     OrderActionEnum(int code){
 *         this.code = code;
 *     }
 * }
 * 
 * public static class OrderActionEnumDeser implements ObjectDeserializer {
 *     
 *     public &lt;T&gt; T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
 *         Integer intValue = parser.parseObject(int.class);
 *         if (intValue == 1) {
 *             return (T) OrderActionEnum.FAIL;
 *         } else if (intValue == 0) {
 *             return (T) OrderActionEnum.SUCC;
 *         }
 *         throw new IllegalStateException();
 *     }
 * 
 *     
 *     public int getFastMatchToken() {
 *         return JSONToken.LITERAL_INT;
 *     }
 * }
 * </pre>
 * 
 * <p>You will also need to register {@code OrderActionEnumDeser} to ParserConfig:</p>
 * <pre>
 * ParserConfig.getGlobalInstance().putDeserializer(OrderActionEnum.class, new OrderActionEnumDeser());
 * </pre>
 */
public interface ObjectDeserializer {
    /**
     * fastjson invokes this call-back method during deserialization when it encounters a field of the
     * specified type.
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JSON#parseObject(String, Type, Feature[])} method to create objects
     * for any non-trivial field of the returned object. 
     *
     * @param parser context DefaultJSONParser being deserialized
     * @param type The type of the Object to deserialize to
     * @param fieldName parent object field name
     * @return a deserialized object of the specified type which is a subclass of {@code T}
     */
    <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName);
    
    int getFastMatchToken();
}
