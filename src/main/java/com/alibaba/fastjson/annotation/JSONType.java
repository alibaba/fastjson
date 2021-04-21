package com.alibaba.fastjson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface JSONType {

    boolean asm() default true;

    String[] orders() default {};

    /**
     * @since 1.2.6
     */
    String[] includes() default {};

    String[] ignores() default {};

    SerializerFeature[] serialzeFeatures() default {};
    Feature[] parseFeatures() default {};
    
    boolean alphabetic() default true;
    
    Class<?> mappingTo() default Void.class;
    
    Class<?> builder() default Void.class;
    
    /**
     * @since 1.2.11
     */
    String typeName() default "";

    /**
     * @since 1.2.32
     */
    String typeKey() default "";
    
    /**
     * @since 1.2.11
     */
    Class<?>[] seeAlso() default{};
    
    /**
     * @since 1.2.14
     */
    Class<?> serializer() default Void.class;
    
    /**
     * @since 1.2.14
     */
    Class<?> deserializer() default Void.class;

    boolean serializeEnumAsJavaBean() default false;

    PropertyNamingStrategy naming() default PropertyNamingStrategy.NeverUseThisValueExceptDefaultValue;

    /**
     * @since 1.2.49
     */
    Class<? extends SerializeFilter>[] serialzeFilters() default {};

    /**
     * @since 1.2.71
     * @return
     */
    Class<? extends ParserConfig.AutoTypeCheckHandler> autoTypeCheckHandler() default ParserConfig.AutoTypeCheckHandler.class;
}
