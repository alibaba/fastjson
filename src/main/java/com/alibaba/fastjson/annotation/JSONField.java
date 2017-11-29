/*
 * Copyright 1999-2017 Alibaba Group.
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
package com.alibaba.fastjson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
public @interface JSONField {
    /**
     * config encode/decode ordinal
     * @since 1.1.42
     * @return
     */
    int ordinal() default 0;

    String name() default "";

    String format() default "";

    boolean serialize() default true;

    boolean deserialize() default true;

    SerializerFeature[] serialzeFeatures() default {};

    Feature[] parseFeatures() default {};
    
    String label() default "";
    
    /**
     * @since 1.2.12
     */
    boolean jsonDirect() default false;
    
    /**
     * Serializer class to use for serializing associated value.
     * 
     * @since 1.2.16
     */
    Class<?> serializeUsing() default Void.class;
    
    /**
     * Deserializer class to use for deserializing associated value. 
     * 
     * @since 1.2.16 
     */
    Class<?> deserializeUsing() default Void.class;

    /**
     * @since 1.2.21
     * @return the alternative names of the field when it is deserialized
     */
    String[] alternateNames() default {};

    /**
     * @since 1.2.31
     */
    boolean unwrapped() default false;
}
