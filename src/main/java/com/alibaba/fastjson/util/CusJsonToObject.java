package com.alibaba.fastjson.util;

/**
 * @author GrainRain
 * @date 2020/03/29 11:51
 **/
public interface CusJsonToObject<E> {
    E getInstance(Object json);
}
