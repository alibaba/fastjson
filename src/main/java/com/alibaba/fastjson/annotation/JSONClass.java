package com.alibaba.fastjson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface JSONClass {
    String[] orders() default {};
    
    SerializerFeature[] serialzeFeatures() default {};
}
