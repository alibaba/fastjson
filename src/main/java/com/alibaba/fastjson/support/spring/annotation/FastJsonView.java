package com.alibaba.fastjson.support.spring.annotation;

import java.lang.annotation.*;

/**
 * <pre>
 * 一个放置到 {@link org.springframework.stereotype.Controller Controller}方法上的注解.
 * 设置返回对象针对某个类需要排除或者包括的字段
 * 例如：
 * <code>&#064;FastJsonView(exclude = {&#064;FastJsonFilter(clazz = JSON.class,props = {"data"})})</code>
 *
 * </pre>
 * @author yanquanyu
 * @author liuming
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FastJsonView {
    FastJsonFilter[] include() default {};
    FastJsonFilter[] exclude() default {};
}
