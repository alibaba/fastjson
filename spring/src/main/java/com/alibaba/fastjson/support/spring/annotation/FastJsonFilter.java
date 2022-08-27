package com.alibaba.fastjson.support.spring.annotation;

import java.lang.annotation.*;

/**
 * <pre>
 * 设置过滤对象对应的class和对应的属性
 * </pre>
 * @author yanquanyu
 * @author liuming
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FastJsonFilter {
    Class<?> clazz();
    String[] props();
}
