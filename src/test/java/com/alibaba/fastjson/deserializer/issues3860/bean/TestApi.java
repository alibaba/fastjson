package com.alibaba.fastjson.deserializer.issues3860.bean;

import java.util.List;

/**
 * @author Archer
 * @date 2022/05/08
 */
public interface TestApi {


    GenericClassA<List<String>, String> getGenericClassA();
}
