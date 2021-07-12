package com.alibaba.fastjson.spi;

/**
 * @project fastjson
 * @desc: propertyNamingStrategy interface
 * @date 2021-06-03 11:17
 */
public interface IPropertyNamingStrategy {
    
    public String translate(String propertyName);
}
