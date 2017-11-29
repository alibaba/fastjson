package com.alibaba.fastjson.support.spring.annotation;

import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

/**
 * Created by SongLing.Dong on 7/22/2017.
 * @see com.alibaba.fastjson.support.spring.JSONPResponseBodyAdvice
 */
@Documented
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ResponseBody
public @interface ResponseJSONP {
    /**
     * The parameter's name of the callback method.
     */
    String callback() default "callback";
}
